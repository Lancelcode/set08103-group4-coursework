package com.napier.sem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

/**
 * Population reporting application.
 * Connects to the MySQL World database and prints reports.
 */
public class App
{
    /**
     * Database connection.
     */
    private Connection con = null;

    /**
     * Entry point. Connects to the database, then shows the report menu.
     * Run with --all to print every report once with sample input
     * (used in Docker and CI, where nobody can type a choice).
     *
     * @param args optional "--all"
     */
    public static void main(String[] args)
    {
        App app = new App();

        app.connect();

        // Exit with an error code so CI fails if the database is not reachable
        if (!app.isConnected())
        {
            System.out.println("Exiting: no database connection");
            System.exit(1);
        }

        Menu menu = new Menu(app, new Scanner(System.in));

        if (args.length > 0 && args[0].equals("--all"))
        {
            menu.runAll();
        }
        else
        {
            menu.run();
        }

        app.disconnect();
    }

    /**
     * Checks whether the app is connected to the database.
     *
     * @return true if connected
     */
    public boolean isConnected()
    {
        return con != null;
    }

    /**
     * Connects to the World database, retrying until MySQL is ready.
     * DB_HOST is used when running through Docker/CI,
     * localhost is used when running locally.
     */
    public void connect()
    {
        String host = System.getenv("DB_HOST");

        if (host == null || host.isEmpty())
        {
            host = "localhost";
        }

        connect(host, 10, 5000);
    }

    /**
     * Connects to the World database, retrying a fixed number of times.
     *
     * @param host database host name
     * @param retries how many attempts to make before giving up
     * @param delayMs milliseconds to wait between attempts
     */
    public void connect(String host, int retries, int delayMs)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load MySQL driver");
            return;
        }

        for (int attempt = 1; attempt <= retries; attempt++)
        {
            System.out.println("Connecting to database (attempt "
                    + attempt + " of " + retries + ")...");

            try
            {
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + host
                                + ":3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "root");

                System.out.println("Successfully connected to database");
                return;
            }
            catch (SQLException e)
            {
                System.out.println("Failed to connect: " + e.getMessage());
            }

            // Wait before the next attempt so MySQL has time to start
            try
            {
                Thread.sleep(delayMs);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("Could not connect to database after "
                + retries + " attempts");
    }
    // ------------------------------------------------------------------
    // Shared query helpers
    // ------------------------------------------------------------------

    /**
     * Turns the current row of a ResultSet into an object.
     *
     * @param <T> type of object produced for each row
     */
    @FunctionalInterface
    private interface RowMapper<T>
    {
        /**
         * Builds one object from the current row.
         *
         * @param rset result set positioned on a row
         * @return the object for that row
         * @throws SQLException if a column cannot be read
         */
        T map(ResultSet rset) throws SQLException;
    }

    /**
     * Appended to a query to limit it to the first N rows.
     */
    private static final String LIMIT = " LIMIT ?";

    /**
     * Runs a query and maps every row it returns.
     * Every report goes through this method, so the connection check,
     * parameter binding and error handling live in one place.
     *
     * @param query SQL with one ? per parameter
     * @param description what is being fetched, used in the error message
     * @param mapper turns each row into an object
     * @param params values for the ? placeholders, in order
     * @param <T> type of object in the returned list
     * @return the mapped rows, or an empty list if the query failed
     */
    private <T> ArrayList<T> runQuery(
            String query, String description,
            RowMapper<T> mapper, Object... params)
    {
        ArrayList<T> results = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return results;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            for (int i = 0; i < params.length; i++)
            {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rset = stmt.executeQuery())
            {
                while (rset.next())
                {
                    results.add(mapper.map(rset));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to get " + description);
            System.out.println(e.getMessage());
        }

        return results;
    }

    /**
     * Runs a Top N query. Checks N first, then adds it as the
     * last parameter so it fills the LIMIT ? placeholder.
     *
     * @param query SQL ending in LIMIT ?
     * @param description what is being fetched, used in the error message
     * @param mapper turns each row into an object
     * @param n number of rows to return, must be greater than 0
     * @param filters values for any WHERE placeholders, in order
     * @param <T> type of object in the returned list
     * @return up to N mapped rows, or an empty list if N is invalid
     */
    private <T> ArrayList<T> runTopNQuery(
            String query, String description,
            RowMapper<T> mapper, int n, Object... filters)
    {
        if (con == null)
        {
            System.out.println("No database connection");
            return new ArrayList<>();
        }

        if (n <= 0)
        {
            System.out.println("N must be greater than 0");
            return new ArrayList<>();
        }

        Object[] params = Arrays.copyOf(filters, filters.length + 1);
        params[filters.length] = n;

        return runQuery(query, description, mapper, params);
    }

    // ------------------------------------------------------------------
    // Country reports (US01)
    // ------------------------------------------------------------------

    /**
     * Columns and joins shared by every country report.
     * LEFT JOIN keeps countries that have no capital, shown as N/A.
     */
    private static final String COUNTRY_SELECT =
            "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                    + "COALESCE(ci.Name, 'N/A') AS Capital "
                    + "FROM country c "
                    + "LEFT JOIN city ci ON c.Capital = ci.ID ";

    /**
     * Sort order shared by every country report.
     */
    private static final String COUNTRY_ORDER = "ORDER BY c.Population DESC";

    /**
     * Builds a Country from the current row.
     */
    private static Country mapCountry(ResultSet rset) throws SQLException
    {
        Country country = new Country();

        country.code = rset.getString("Code");
        country.name = rset.getString("Name");
        country.continent = rset.getString("Continent");
        country.region = rset.getString("Region");
        country.population = rset.getLong("Population");
        country.capital = rset.getString("Capital");

        return country;
    }

    /**
     * Gets all countries in the world ordered by population.
     *
     * @return list of countries
     */
    public ArrayList<Country> getCountries()
    {
        return runQuery(COUNTRY_SELECT + COUNTRY_ORDER,
                "countries", App::mapCountry);
    }

    /**
     * Gets all countries in a continent ordered by population.
     *
     * @param continent continent name
     * @return matching countries
     */
    public ArrayList<Country> getCountriesByContinent(String continent)
    {
        return runQuery(COUNTRY_SELECT + "WHERE c.Continent = ? " + COUNTRY_ORDER,
                "countries", App::mapCountry, continent);
    }

    /**
     * Gets all countries in a region ordered by population.
     *
     * @param region region name
     * @return matching countries
     */
    public ArrayList<Country> getCountriesByRegion(String region)
    {
        return runQuery(COUNTRY_SELECT + "WHERE c.Region = ? " + COUNTRY_ORDER,
                "countries", App::mapCountry, region);
    }

    /**
     * Gets top N populated countries in the world.
     *
     * @param n number of countries to return
     * @return top N countries
     */
    public ArrayList<Country> getTopNCountries(int n)
    {
        return runTopNQuery(COUNTRY_SELECT + COUNTRY_ORDER + LIMIT,
                "countries", App::mapCountry, n);
    }

    /**
     * Gets top N populated countries in a continent.
     *
     * @param continent continent name
     * @param n number of countries to return
     * @return top N countries in the continent
     */
    public ArrayList<Country> getTopNCountriesByContinent(String continent, int n)
    {
        return runTopNQuery(
                COUNTRY_SELECT + "WHERE c.Continent = ? " + COUNTRY_ORDER + LIMIT,
                "countries", App::mapCountry, n, continent);
    }

    /**
     * Gets top N populated countries in a region.
     *
     * @param region region name
     * @param n number of countries to return
     * @return top N countries in the region
     */
    public ArrayList<Country> getTopNCountriesByRegion(String region, int n)
    {
        return runTopNQuery(
                COUNTRY_SELECT + "WHERE c.Region = ? " + COUNTRY_ORDER + LIMIT,
                "countries", App::mapCountry, n, region);
    }

    // ------------------------------------------------------------------
    // City reports (US02)
    // ------------------------------------------------------------------

    /**
     * Columns and joins shared by every city report.
     */
    private static final String CITY_SELECT =
            "SELECT ci.Name, co.Name AS Country, "
                    + "ci.District, ci.Population "
                    + "FROM city ci "
                    + "JOIN country co ON ci.CountryCode = co.Code ";

    /**
     * Sort order shared by every city report.
     */
    private static final String CITY_ORDER = "ORDER BY ci.Population DESC";

    /**
     * Builds a City from the current row.
     */
    private static City mapCity(ResultSet rset) throws SQLException
    {
        City city = new City();

        city.name = rset.getString("Name");
        city.country = rset.getString("Country");
        city.district = rset.getString("District");
        city.population = rset.getLong("Population");

        return city;
    }

    /**
     * Gets all cities in the world ordered from largest
     * population to smallest.
     *
     * @return list of cities
     */
    public ArrayList<City> getCities()
    {
        return runQuery(CITY_SELECT + CITY_ORDER, "cities", App::mapCity);
    }

    /**
     * Gets all cities in a continent.
     *
     * @param continent continent name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByContinent(String continent)
    {
        return runQuery(CITY_SELECT + "WHERE co.Continent = ? " + CITY_ORDER,
                "cities", App::mapCity, continent);
    }

    /**
     * Gets all cities in a region.
     *
     * @param region region name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByRegion(String region)
    {
        return runQuery(CITY_SELECT + "WHERE co.Region = ? " + CITY_ORDER,
                "cities", App::mapCity, region);
    }

    /**
     * Gets all cities in a country.
     *
     * @param country country name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByCountry(String country)
    {
        return runQuery(CITY_SELECT + "WHERE co.Name = ? " + CITY_ORDER,
                "cities", App::mapCity, country);
    }

    /**
     * Gets all cities in a district.
     *
     * @param district district name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByDistrict(String district)
    {
        return runQuery(CITY_SELECT + "WHERE ci.District = ? " + CITY_ORDER,
                "cities", App::mapCity, district);
    }

    /**
     * Gets the Top N populated cities in the world.
     *
     * @param n number of cities to return
     * @return Top N cities
     */
    public ArrayList<City> getTopNCities(int n)
    {
        return runTopNQuery(CITY_SELECT + CITY_ORDER + LIMIT,
                "cities", App::mapCity, n);
    }

    /**
     * Gets the Top N populated cities in a continent.
     *
     * @param continent continent name
     * @param n number of cities to return
     * @return Top N matching cities
     */
    public ArrayList<City> getTopNCitiesByContinent(
            String continent, int n)
    {
        return runTopNQuery(
                CITY_SELECT + "WHERE co.Continent = ? " + CITY_ORDER + LIMIT,
                "cities", App::mapCity, n, continent);
    }

    /**
     * Gets the Top N populated cities in a region.
     *
     * @param region region name
     * @param n number of cities to return
     * @return Top N matching cities
     */
    public ArrayList<City> getTopNCitiesByRegion(
            String region, int n)
    {
        return runTopNQuery(
                CITY_SELECT + "WHERE co.Region = ? " + CITY_ORDER + LIMIT,
                "cities", App::mapCity, n, region);
    }

    /**
     * Gets the Top N populated cities in a country.
     *
     * @param country country name
     * @param n number of cities to return
     * @return Top N matching cities
     */
    public ArrayList<City> getTopNCitiesByCountry(
            String country, int n)
    {
        return runTopNQuery(
                CITY_SELECT + "WHERE co.Name = ? " + CITY_ORDER + LIMIT,
                "cities", App::mapCity, n, country);
    }

    /**
     * Gets the Top N populated cities in a district.
     *
     * @param district district name
     * @param n number of cities to return
     * @return Top N matching cities
     */
    public ArrayList<City> getTopNCitiesByDistrict(
            String district, int n)
    {
        return runTopNQuery(
                CITY_SELECT + "WHERE ci.District = ? " + CITY_ORDER + LIMIT,
                "cities", App::mapCity, n, district);
    }

    // ------------------------------------------------------------------
    // Capital city reports (US03)
    // ------------------------------------------------------------------

    /**
     * Columns and joins shared by every capital city report.
     * Uses the country's Capital column to find its capital city.
     */
    private static final String CAPITAL_SELECT =
            "SELECT ci.Name, c.Name AS Country, ci.Population "
                    + "FROM country c "
                    + "JOIN city ci ON c.Capital = ci.ID ";

    /**
     * Sort order shared by every capital city report.
     */
    private static final String CAPITAL_ORDER = "ORDER BY ci.Population DESC";

    /**
     * Builds a CapitalCity from the current row.
     */
    private static CapitalCity mapCapitalCity(ResultSet rset) throws SQLException
    {
        CapitalCity capitalCity = new CapitalCity();

        capitalCity.name = rset.getString("Name");
        capitalCity.country = rset.getString("Country");
        capitalCity.population = rset.getLong("Population");

        return capitalCity;
    }

    /**
     * Gets all capital cities in the world, largest population first.
     *
     * @return list of capital cities
     */
    public ArrayList<CapitalCity> getCapitalCities()
    {
        return runQuery(CAPITAL_SELECT + CAPITAL_ORDER,
                "capital cities", App::mapCapitalCity);
    }

    /**
     * Gets all capital cities in a continent.
     *
     * @param continent continent name
     * @return matching capital cities
     */
    public ArrayList<CapitalCity> getCapitalCitiesByContinent(String continent)
    {
        return runQuery(CAPITAL_SELECT + "WHERE c.Continent = ? " + CAPITAL_ORDER,
                "capital cities", App::mapCapitalCity, continent);
    }

    /**
     * Gets all capital cities in a region.
     *
     * @param region region name
     * @return matching capital cities
     */
    public ArrayList<CapitalCity> getCapitalCitiesByRegion(String region)
    {
        return runQuery(CAPITAL_SELECT + "WHERE c.Region = ? " + CAPITAL_ORDER,
                "capital cities", App::mapCapitalCity, region);
    }

    /**
     * Gets the Top N populated capital cities in the world.
     *
     * @param n number of capital cities to return
     * @return Top N capital cities
     */
    public ArrayList<CapitalCity> getTopNCapitalCities(int n)
    {
        return runTopNQuery(CAPITAL_SELECT + CAPITAL_ORDER + LIMIT,
                "capital cities", App::mapCapitalCity, n);
    }

    /**
     * Gets the Top N populated capital cities in a continent.
     *
     * @param continent continent name
     * @param n number of capital cities to return
     * @return Top N matching capital cities
     */
    public ArrayList<CapitalCity> getTopNCapitalCitiesByContinent(
            String continent, int n)
    {
        return runTopNQuery(
                CAPITAL_SELECT + "WHERE c.Continent = ? " + CAPITAL_ORDER + LIMIT,
                "capital cities", App::mapCapitalCity, n, continent);
    }

    /**
     * Gets the Top N populated capital cities in a region.
     *
     * @param region region name
     * @param n number of capital cities to return
     * @return Top N matching capital cities
     */
    public ArrayList<CapitalCity> getTopNCapitalCitiesByRegion(
            String region, int n)
    {
        return runTopNQuery(
                CAPITAL_SELECT + "WHERE c.Region = ? " + CAPITAL_ORDER + LIMIT,
                "capital cities", App::mapCapitalCity, n, region);
    }

    // ------------------------------------------------------------------
    // Population totals (US05)
    // ------------------------------------------------------------------

    /**
     * Builds a PopulationTotal from a row with Name and TotalPopulation.
     */
    private static PopulationTotal mapPopulationTotal(ResultSet rset)
            throws SQLException
    {
        return populationTotal(
                rset.getString("Name"), rset.getLong("TotalPopulation"));
    }

    /**
     * Creates a PopulationTotal.
     *
     * @param name label to show for the location
     * @param population population of the location
     * @return the new population total
     */
    private static PopulationTotal populationTotal(String name, long population)
    {
        PopulationTotal total = new PopulationTotal();
        total.name = name;
        total.population = population;
        return total;
    }

    /**
     * Gets the total population of the world.
     *
     * @return the total world population
     */
    public long getWorldPopulation()
    {
        ArrayList<Long> rows = runQuery(
                "SELECT SUM(Population) AS TotalPopulation FROM country",
                "world population",
                rset -> rset.getLong("TotalPopulation"));

        return rows.isEmpty() ? 0 : rows.get(0);
    }

    /**
     * Gets the total population of a specific continent.
     *
     * @param continent name of the continent
     * @return matching population totals
     */
    public ArrayList<PopulationTotal> getPopulationByContinent(String continent)
    {
        return runQuery(
                "SELECT Continent AS Name, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "WHERE Continent = ? "
                        + "GROUP BY Continent",
                "continent population", App::mapPopulationTotal, continent);
    }

    /**
     * Gets the total population of a specific region.
     *
     * @param region name of the region
     * @return matching population totals
     */
    public ArrayList<PopulationTotal> getPopulationByRegion(String region)
    {
        return runQuery(
                "SELECT Region AS Name, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "WHERE Region = ? "
                        + "GROUP BY Region",
                "region population", App::mapPopulationTotal, region);
    }

    /**
     * Gets the population of a specific country.
     *
     * @param country name of the country
     * @return matching population totals
     */
    public ArrayList<PopulationTotal> getPopulationByCountry(String country)
    {
        return runQuery(
                "SELECT Name, Population AS TotalPopulation "
                        + "FROM country "
                        + "WHERE Name = ?",
                "country population", App::mapPopulationTotal, country);
    }

    /**
     * Gets the population of a district within a specific country.
     * District population is the sum of its recorded cities.
     *
     * @param district name of the district
     * @param country name of the country
     * @return matching district population totals
     */
    public ArrayList<PopulationTotal> getPopulationByDistrict(
            String district, String country)
    {
        return runQuery(
                "SELECT ci.District AS Name, "
                        + "SUM(ci.Population) AS TotalPopulation "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE ci.District = ? AND co.Name = ? "
                        + "GROUP BY ci.District",
                "district population",
                rset -> populationTotal(
                        country + " - " + rset.getString("Name"),
                        rset.getLong("TotalPopulation")),
                district, country);
    }

    /**
     * Gets the population of a city within a specific country.
     * More than one row means cities with the same name in different
     * districts, so the district is shown to tell them apart.
     *
     * @param city name of the city
     * @param country name of the country
     * @return matching city population totals
     */
    public ArrayList<PopulationTotal> getPopulationByCity(
            String city, String country)
    {
        return runQuery(
                "SELECT ci.Name, ci.District, "
                        + "ci.Population AS TotalPopulation "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE ci.Name = ? AND co.Name = ? "
                        + "ORDER BY ci.District",
                "city population",
                rset -> populationTotal(
                        country + " - " + rset.getString("Name")
                                + " (" + rset.getString("District") + ")",
                        rset.getLong("TotalPopulation")),
                city, country);
    }

    /**
     * Gets the total population for each continent.
     *
     * @return a list containing one population total for each continent
     */
    public ArrayList<PopulationTotal> getContinentPopulations()
    {
        return runQuery(
                "SELECT Continent AS Name, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "GROUP BY Continent "
                        + "ORDER BY TotalPopulation DESC",
                "continent populations", App::mapPopulationTotal);
    }

    /**
     * Gets the total population for each region.
     *
     * @return a list containing one population total for each region
     */
    public ArrayList<PopulationTotal> getRegionPopulations()
    {
        return runQuery(
                "SELECT Region AS Name, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "GROUP BY Region "
                        + "ORDER BY TotalPopulation DESC",
                "region populations", App::mapPopulationTotal);
    }

    /**
     * Gets the total population for each district within each country.
     *
     * @return a list containing district population totals
     */
    public ArrayList<PopulationTotal> getDistrictPopulations()
    {
        return runQuery(
                "SELECT c.Name AS Country, ci.District, "
                        + "SUM(ci.Population) AS TotalPopulation "
                        + "FROM city ci "
                        + "JOIN country c ON ci.CountryCode = c.Code "
                        + "GROUP BY c.Name, ci.District "
                        + "ORDER BY TotalPopulation DESC",
                "district populations",
                rset -> populationTotal(
                        rset.getString("Country") + " - "
                                + rset.getString("District"),
                        rset.getLong("TotalPopulation")));
    }

    /**
     * Gets the population of each city.
     *
     * @return a list containing the population of each city
     */
    public ArrayList<PopulationTotal> getCityPopulations()
    {
        return runQuery(
                "SELECT c.Name AS Country, ci.Name AS City, ci.Population "
                        + "FROM city ci "
                        + "JOIN country c ON ci.CountryCode = c.Code "
                        + "ORDER BY ci.Population DESC",
                "city populations",
                rset -> populationTotal(
                        rset.getString("Country") + " - "
                                + rset.getString("City"),
                        rset.getLong("Population")));
    }

    // ------------------------------------------------------------------
    // Language statistics (US06)
    // ------------------------------------------------------------------

    /**
     * Gets the estimated number of speakers of Chinese, English, Hindi,
     * Spanish and Arabic, from most to fewest speakers.
     * Speakers in each country = country population x language percentage.
     *
     * @return one statistic per language, largest first
     */
    public ArrayList<LanguageStatistic> getLanguageStatistics()
    {
        // Percentage is stored as e.g. 25.5, so divide by 100.
        // The world total is worked out once in the sub-query.
        String query =
                "SELECT cl.Language, "
                        + "ROUND(SUM(c.Population * cl.Percentage / 100)) AS Speakers, "
                        + "SUM(c.Population * cl.Percentage / 100) "
                        + "/ (SELECT SUM(Population) FROM country) * 100 AS WorldPercentage "
                        + "FROM countrylanguage cl "
                        + "JOIN country c ON cl.CountryCode = c.Code "
                        + "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') "
                        + "GROUP BY cl.Language "
                        + "ORDER BY Speakers DESC";

        return runQuery(query, "language statistics", rset ->
        {
            LanguageStatistic stat = new LanguageStatistic();
            stat.language = rset.getString("Language");
            stat.speakers = rset.getLong("Speakers");
            stat.worldPercentage = rset.getDouble("WorldPercentage");
            return stat;
        });
    }

    // ------------------------------------------------------------------
    // City / non-city breakdown (US04)
    // ------------------------------------------------------------------

    /**
     * Joins each country to its total city population.
     * City totals are grouped per country before the join,
     * so each country's population is only counted once.
     */
    private static final String BREAKDOWN_FROM =
            "FROM country co "
                    + "LEFT JOIN ("
                    + "SELECT CountryCode, SUM(Population) AS CityPopulation "
                    + "FROM city GROUP BY CountryCode"
                    + ") cp ON cp.CountryCode = co.Code ";

    /**
     * Sort order shared by every breakdown report.
     */
    private static final String BREAKDOWN_ORDER = "ORDER BY TotalPopulation DESC, Name";

    /**
     * Builds a PopulationBreakdown from the current row.
     */
    private static PopulationBreakdown mapBreakdown(ResultSet rset)
            throws SQLException
    {
        PopulationBreakdown breakdown = new PopulationBreakdown();
        breakdown.name = rset.getString("Name");
        breakdown.totalPopulation = rset.getLong("TotalPopulation");
        breakdown.cityPopulation = rset.getLong("CityPopulation");
        return breakdown;
    }

    /**
     * Gets city and non-city population totals for each continent.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getContinentBreakdowns()
    {
        return getGroupedBreakdowns("co.Continent");
    }

    /**
     * Gets city and non-city population totals for each region.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getRegionBreakdowns()
    {
        return getGroupedBreakdowns("co.Region");
    }

    /**
     * Gets city and non-city population totals for each country.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getCountryBreakdowns()
    {
        return runQuery(
                "SELECT co.Name AS Name, "
                        + "co.Population AS TotalPopulation, "
                        + "COALESCE(cp.CityPopulation, 0) AS CityPopulation "
                        + BREAKDOWN_FROM
                        + BREAKDOWN_ORDER,
                "population breakdown", App::mapBreakdown);
    }

    /**
     * Gets breakdowns summed over a country column (continent or region).
     * The column is one of our own constants, never user input,
     * so it is safe to put into the SQL text.
     *
     * @param column country column to group by
     * @return population breakdowns, largest total population first
     */
    private ArrayList<PopulationBreakdown> getGroupedBreakdowns(String column)
    {
        return runQuery(
                "SELECT " + column + " AS Name, "
                        + "SUM(co.Population) AS TotalPopulation, "
                        + "SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation "
                        + BREAKDOWN_FROM
                        + "GROUP BY " + column + " "
                        + BREAKDOWN_ORDER,
                "population breakdown", App::mapBreakdown);
    }

    /**
     * Prints a country report.
     *
     * @param countries the countries to print
     */
    public void displayCountries(ArrayList<Country> countries)
    {
        if (countries == null)
        {
            System.out.println("No countries to display");
            return;
        }
        System.out.println(
                "Code | Name | Continent | Region | Population | Capital");

        for (Country country : countries)
        {
            System.out.println(
                    country.code + " | "
                            + country.name + " | "
                            + country.continent + " | "
                            + country.region + " | "
                            + country.population + " | "
                            + country.capital);
        }
    }

    /**
     * Prints the language report.
     *
     * @param stats language statistics to print
     */
    public void displayLanguageStatistics(ArrayList<LanguageStatistic> stats)
    {
        System.out.println("Language | Speakers | % of world population");

        for (LanguageStatistic stat : stats)
        {
            System.out.println(
                    stat.language + " | "
                            + stat.speakers + " | "
                            + String.format("%.2f", stat.worldPercentage) + "%");
        }
    }

    /**
     * Prints a city report.
     *
     * @param cities the cities to print
     */
    public void displayCities(ArrayList<City> cities)
    {
        System.out.println(
                "Name | Country | District | Population");

        for (City city : cities)
        {
            System.out.println(
                    city.name + " | "
                            + city.country + " | "
                            + city.district + " | "
                            + city.population);
        }
    }

    /**
     * Prints a capital city report.
     *
     * @param capitalCities capital cities to print
     */
    public void displayCapitalCities(ArrayList<CapitalCity> capitalCities)
    {
        System.out.println("Name | Country | Population");

        for (CapitalCity capitalCity : capitalCities)
        {
            System.out.println(
                    capitalCity.name + " | "
                            + capitalCity.country + " | "
                            + capitalCity.population);
        }
    }

    /**
     * Closes the database connection.
     */
    public void disconnect()
    {
        try
        {
            if (con != null)
            {
                con.close();
            }
        }
        catch (Exception e)
        {
            System.out.println(
                    "Error closing database connection");
        }
    }

    /**
     * Prints population totals and city and non-city percentages.
     *
     * @param breakdowns population breakdown rows to print
     */
    public void displayPopulationBreakdowns(
            ArrayList<PopulationBreakdown> breakdowns)
    {
        System.out.println(
                "Name | Total Population | City Population | City % "
                        + "| Non-city Population | Non-city %");

        for (PopulationBreakdown breakdown : breakdowns)
        {
            System.out.printf(
                    Locale.UK,
                    "%s | %d | %d | %.2f%% | %d | %.2f%%%n",
                    breakdown.name,
                    breakdown.totalPopulation,
                    breakdown.cityPopulation,
                    breakdown.getCityPercentage(),
                    breakdown.getNonCityPopulation(),
                    breakdown.getNonCityPercentage());
        }
    }}