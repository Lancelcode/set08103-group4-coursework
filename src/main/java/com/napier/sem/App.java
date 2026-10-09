package com.napier.sem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
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

    /**
     * Gets all countries in the world ordered by population.
     *
     * @return list of countries
     */
    public ArrayList<Country> getCountries()
    {
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "ORDER BY c.Population DESC";

        return getCountriesFromQuery(query);
    }



    /**
     * Gets all countries in a continent ordered by population.
     *
     * @param continent continent name
     * @return matching countries
     */
    public ArrayList<Country> getCountriesByContinent(String continent)
    {
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Continent = ? "
                        + "ORDER BY c.Population DESC";

        return getCountriesFromQuery(query, continent);
    }

    /**
     * Gets all countries in a region ordered by population.
     *
     * @param region region name
     * @return matching countries
     */
    public ArrayList<Country> getCountriesByRegion(String region)
    {
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Region = ? "
                        + "ORDER BY c.Population DESC";

        return getCountriesFromQuery(query, region);
    }

    /**
     * Gets top N populated countries in the world.
     *
     * @param n number of countries to return
     * @return top N countries
     */
    public ArrayList<Country> getTopNCountries(int n)
    {
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "ORDER BY c.Population DESC "
                        + "LIMIT ?";

        return getTopNCountriesFromQuery(query, n);
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
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Continent = ? "
                        + "ORDER BY c.Population DESC "
                        + "LIMIT ?";

        return getTopNCountriesFromQuery(query, continent, n);
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
        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Region = ? "
                        + "ORDER BY c.Population DESC "
                        + "LIMIT ?";

        return getTopNCountriesFromQuery(query, region, n);
    }

    /**
     * Executes a country query with no filter.
     */
    private ArrayList<Country> getCountriesFromQuery(String query)
    {
        ArrayList<Country> countries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return countries;
        }

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery())
        {
            addCountriesFromResultSet(rset, countries);
        }
        catch (Exception e)
        {
            System.out.println("Failed to get countries");
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Executes a country query with one text filter.
     */
    private ArrayList<Country> getCountriesFromQuery(
            String query, String value)
    {
        ArrayList<Country> countries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return countries;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCountriesFromResultSet(rset, countries);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get countries");
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Executes a Top N country query with no text filter.
     */
    private ArrayList<Country> getTopNCountriesFromQuery(
            String query, int n)
    {
        ArrayList<Country> countries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return countries;
        }

        if (n <= 0)
        {
            System.out.println("N must be greater than 0");
            return countries;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCountriesFromResultSet(rset, countries);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get countries");
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Executes a Top N country query with one text filter.
     */
    private ArrayList<Country> getTopNCountriesFromQuery(
            String query, String value, int n)
    {
        ArrayList<Country> countries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return countries;
        }

        if (n <= 0)
        {
            System.out.println("N must be greater than 0");
            return countries;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCountriesFromResultSet(rset, countries);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get countries");
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Converts database rows into Country objects.
     */
    private void addCountriesFromResultSet(
            ResultSet rset, ArrayList<Country> countries) throws Exception
    {
        while (rset.next())
        {
            Country country = new Country();

            country.code = rset.getString("Code");
            country.name = rset.getString("Name");
            country.continent = rset.getString("Continent");
            country.region = rset.getString("Region");
            country.population = rset.getLong("Population");
            country.capital = rset.getString("Capital");

            countries.add(country);
        }
    }

    /**
     * Gets all cities in the world ordered from largest
     * population to smallest.
     *
     * @return list of cities
     */
    public ArrayList<City> getCities()
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "ORDER BY ci.Population DESC";

        return getCitiesFromQuery(query);
    }

    /**
     * Gets all cities in a continent.
     *
     * @param continent continent name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByContinent(String continent)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Continent = ? "
                        + "ORDER BY ci.Population DESC";

        return getCitiesFromQuery(query, continent);
    }

    /**
     * Gets all cities in a region.
     *
     * @param region region name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByRegion(String region)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Region = ? "
                        + "ORDER BY ci.Population DESC";

        return getCitiesFromQuery(query, region);
    }

    /**
     * Gets all cities in a country.
     *
     * @param country country name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByCountry(String country)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Name = ? "
                        + "ORDER BY ci.Population DESC";

        return getCitiesFromQuery(query, country);
    }

    /**
     * Gets all cities in a district.
     *
     * @param district district name
     * @return matching cities
     */
    public ArrayList<City> getCitiesByDistrict(String district)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE ci.District = ? "
                        + "ORDER BY ci.Population DESC";

        return getCitiesFromQuery(query, district);
    }

    /**
     * Gets the Top N populated cities in the world.
     *
     * @param n number of cities to return
     * @return Top N cities
     */
    public ArrayList<City> getTopNCities(int n)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCitiesFromQuery(query, n);
    }

    /**
     * Gets the Top N populated cities in a continent.
     */
    public ArrayList<City> getTopNCitiesByContinent(
            String continent, int n)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Continent = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCitiesFromQuery(query, continent, n);
    }

    /**
     * Gets the Top N populated cities in a region.
     */
    public ArrayList<City> getTopNCitiesByRegion(
            String region, int n)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Region = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCitiesFromQuery(query, region, n);
    }

    /**
     * Gets the Top N populated cities in a country.
     */
    public ArrayList<City> getTopNCitiesByCountry(
            String country, int n)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE co.Name = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCitiesFromQuery(query, country, n);
    }

    /**
     * Gets the Top N populated cities in a district.
     */
    public ArrayList<City> getTopNCitiesByDistrict(
            String district, int n)
    {
        String query =
                "SELECT ci.Name, co.Name AS Country, "
                        + "ci.District, ci.Population "
                        + "FROM city ci "
                        + "JOIN country co ON ci.CountryCode = co.Code "
                        + "WHERE ci.District = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCitiesFromQuery(query, district, n);
    }

    /**
     * Executes a city query with no filter.
     */
    private ArrayList<City> getCitiesFromQuery(String query)
    {
        ArrayList<City> cities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return cities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery())
        {
            addCitiesFromResultSet(rset, cities);
        }
        catch (Exception e)
        {
            System.out.println("Failed to get cities");
            System.out.println(e.getMessage());
        }

        return cities;
    }

    /**
     * Executes a city query with one text filter.
     */
    private ArrayList<City> getCitiesFromQuery(
            String query, String value)
    {
        ArrayList<City> cities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return cities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCitiesFromResultSet(rset, cities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get cities");
            System.out.println(e.getMessage());
        }

        return cities;
    }

    /**
     * Executes a Top N city query with no text filter.
     */
    private ArrayList<City> getTopNCitiesFromQuery(
            String query, int n)
    {
        ArrayList<City> cities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return cities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCitiesFromResultSet(rset, cities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get cities");
            System.out.println(e.getMessage());
        }

        return cities;
    }

    /**
     * Executes a Top N city query with one text filter.
     */
    private ArrayList<City> getTopNCitiesFromQuery(
            String query, String value, int n)
    {
        ArrayList<City> cities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return cities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCitiesFromResultSet(rset, cities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get cities");
            System.out.println(e.getMessage());
        }

        return cities;
    }

    /**
     * Converts database rows into City objects.
     */
    private void addCitiesFromResultSet(
            ResultSet rset, ArrayList<City> cities) throws Exception
    {
        while (rset.next())
        {
            City city = new City();

            city.name = rset.getString("Name");
            city.country = rset.getString("Country");
            city.district = rset.getString("District");
            city.population = rset.getLong("Population");

            cities.add(city);
        }
    }

    /**
     * Prints a country report.
     */
    public void displayCountries(ArrayList<Country> countries)
    {
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
     * Gets the total population of the world.
     *
     * @return the total world population
     */
    public long getWorldPopulation()
    {
        if (con == null)
        {
            System.out.println("No database connection");
            return 0;
        }

        String query = "SELECT SUM(Population) AS TotalPopulation FROM country";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
        {
            if (rset.next())
            {
                return rset.getLong("TotalPopulation");
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get world population");
            System.out.println(e.getMessage());
        }

        return 0;
    }


    /**
     * Gets the total population for each continent.
     *
     * @return a list containing one population total for each continent
     */
    public ArrayList<PopulationTotal> getContinentPopulations()
    {
        ArrayList<PopulationTotal> totals = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return totals;
        }

        String query =
                "SELECT Continent, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "GROUP BY Continent "
                        + "ORDER BY TotalPopulation DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
        {
            while (rset.next())
            {
                PopulationTotal total = new PopulationTotal();
                total.name = rset.getString("Continent");
                total.population = rset.getLong("TotalPopulation");
                totals.add(total);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get continent populations");
            System.out.println(e.getMessage());
        }

        return totals;
    }

    /**
     * Gets the total population for each region.
     *
     * @return a list containing one population total for each region
     */
    public ArrayList<PopulationTotal> getRegionPopulations()
    {
        ArrayList<PopulationTotal> totals = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return totals;
        }

        String query =
                "SELECT Region, SUM(Population) AS TotalPopulation "
                        + "FROM country "
                        + "GROUP BY Region "
                        + "ORDER BY TotalPopulation DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
        {
            while (rset.next())
            {
                PopulationTotal total = new PopulationTotal();
                total.name = rset.getString("Region");
                total.population = rset.getLong("TotalPopulation");
                totals.add(total);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get region populations");
            System.out.println(e.getMessage());
        }

        return totals;
    }

    /**
     * Gets the total population for each district within each country.
     *
     * @return a list containing district population totals
     */
    public ArrayList<PopulationTotal> getDistrictPopulations()
    {
        ArrayList<PopulationTotal> totals = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return totals;
        }

        String query =
                "SELECT c.Name AS Country, ci.District, "
                        + "SUM(ci.Population) AS TotalPopulation "
                        + "FROM city ci "
                        + "JOIN country c ON ci.CountryCode = c.Code "
                        + "GROUP BY c.Name, ci.District "
                        + "ORDER BY TotalPopulation DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
        {
            while (rset.next())
            {
                PopulationTotal total = new PopulationTotal();

                total.name = rset.getString("Country")
                        + " - "
                        + rset.getString("District");

                total.population = rset.getLong("TotalPopulation");

                totals.add(total);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get district populations");
            System.out.println(e.getMessage());
        }

        return totals;
    }

    /**
     * Gets the population of each city.
     *
     * @return a list containing the population of each city
     */
    public ArrayList<PopulationTotal> getCityPopulations()
    {
        ArrayList<PopulationTotal> totals = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return totals;
        }

        String query =
                "SELECT c.Name AS Country, ci.Name AS City, ci.Population "
                        + "FROM city ci "
                        + "JOIN country c ON ci.CountryCode = c.Code "
                        + "ORDER BY ci.Population DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
        {
            while (rset.next())
            {
                PopulationTotal total = new PopulationTotal();

                total.name = rset.getString("Country")
                        + " - "
                        + rset.getString("City");

                total.population = rset.getLong("Population");

                totals.add(total);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get city populations");
            System.out.println(e.getMessage());
        }

        return totals;
    }


    /**
     * Prints a city report.
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
     * Gets all capital cities in the world, largest population first.
     *
     * @return list of capital cities
     */
    public ArrayList<CapitalCity> getCapitalCities()
    {
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "ORDER BY ci.Population DESC";

        return getCapitalCitiesFromQuery(query);
    }

    /**
     * Gets all capital cities in a continent.
     *
     * @param continent continent name
     * @return matching capital cities
     */
    public ArrayList<CapitalCity> getCapitalCitiesByContinent(String continent)
    {
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Continent = ? "
                        + "ORDER BY ci.Population DESC";

        return getCapitalCitiesFromQuery(query, continent);
    }

    /**
     * Gets all capital cities in a region.
     *
     * @param region region name
     * @return matching capital cities
     */
    public ArrayList<CapitalCity> getCapitalCitiesByRegion(String region)
    {
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Region = ? "
                        + "ORDER BY ci.Population DESC";

        return getCapitalCitiesFromQuery(query, region);
    }

    /**
     * Gets the Top N populated capital cities in the world.
     *
     * @param n number of capital cities to return
     * @return Top N capital cities
     */
    public ArrayList<CapitalCity> getTopNCapitalCities(int n)
    {
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCapitalCitiesFromQuery(query, n);
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
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Continent = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCapitalCitiesFromQuery(query, continent, n);
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
        String query =
                "SELECT ci.Name, c.Name AS Country, ci.Population "
                        + "FROM country c "
                        + "JOIN city ci ON c.Capital = ci.ID "
                        + "WHERE c.Region = ? "
                        + "ORDER BY ci.Population DESC "
                        + "LIMIT ?";

        return getTopNCapitalCitiesFromQuery(query, region, n);
    }

    /**
     * Executes a capital city query with no filter.
     *
     * @param query SQL query to execute
     * @return capital cities returned by the query
     */
    private ArrayList<CapitalCity> getCapitalCitiesFromQuery(String query)
    {
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return capitalCities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery())
        {
            addCapitalCitiesFromResultSet(rset, capitalCities);
        }
        catch (Exception e)
        {
            System.out.println("Failed to get capital cities");
            System.out.println(e.getMessage());
        }

        return capitalCities;
    }

    /**
     * Executes a capital city query with one text filter.
     *
     * @param query SQL query to execute
     * @param value filter value
     * @return capital cities returned by the query
     */
    private ArrayList<CapitalCity> getCapitalCitiesFromQuery(
            String query, String value)
    {
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return capitalCities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCapitalCitiesFromResultSet(rset, capitalCities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get capital cities");
            System.out.println(e.getMessage());
        }

        return capitalCities;
    }

    /**
     * Executes a Top N capital city query.
     *
     * @param query SQL query to execute
     * @param n number of results to return
     * @return Top N capital cities
     */
    private ArrayList<CapitalCity> getTopNCapitalCitiesFromQuery(
            String query, int n)
    {
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return capitalCities;
        }

        if (n <= 0)
        {
            System.out.println("N must be greater than 0");
            return capitalCities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setInt(1, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCapitalCitiesFromResultSet(rset, capitalCities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get capital cities");
            System.out.println(e.getMessage());
        }

        return capitalCities;
    }

    /**
     * Executes a filtered Top N capital city query.
     *
     * @param query SQL query to execute
     * @param value filter value
     * @param n number of results to return
     * @return Top N matching capital cities
     */
    private ArrayList<CapitalCity> getTopNCapitalCitiesFromQuery(
            String query, String value, int n)
    {
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return capitalCities;
        }

        if (n <= 0)
        {
            System.out.println("N must be greater than 0");
            return capitalCities;
        }

        try (PreparedStatement stmt = con.prepareStatement(query))
        {
            stmt.setString(1, value);
            stmt.setInt(2, n);

            try (ResultSet rset = stmt.executeQuery())
            {
                addCapitalCitiesFromResultSet(rset, capitalCities);
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to get capital cities");
            System.out.println(e.getMessage());
        }

        return capitalCities;
    }

    /**
     * Converts database rows into CapitalCity objects.
     *
     * @param rset query results
     * @param capitalCities list to populate
     * @throws Exception if database data cannot be read
     */
    private void addCapitalCitiesFromResultSet(
            ResultSet rset, ArrayList<CapitalCity> capitalCities) throws Exception
    {
        while (rset.next())
        {
            CapitalCity capitalCity = new CapitalCity();

            capitalCity.name = rset.getString("Name");
            capitalCity.country = rset.getString("Country");
            capitalCity.population = rset.getLong("Population");

            capitalCities.add(capitalCity);
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
     * Gets city and non-city population totals for each continent.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getContinentBreakdowns()
    {
        String query =
                "SELECT co.Continent AS Name, "
                        + "SUM(co.Population) AS TotalPopulation, "
                        + "SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation "
                        + "FROM country co "
                        + "LEFT JOIN ("
                        + "SELECT CountryCode, SUM(Population) AS CityPopulation "
                        + "FROM city GROUP BY CountryCode"
                        + ") cp ON cp.CountryCode = co.Code "
                        + "GROUP BY co.Continent "
                        + "ORDER BY TotalPopulation DESC, Name";

        return getPopulationBreakdowns(query);
    }

    /**
     * Gets city and non-city population totals for each region.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getRegionBreakdowns()
    {
        String query =
                "SELECT co.Region AS Name, "
                        + "SUM(co.Population) AS TotalPopulation, "
                        + "SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation "
                        + "FROM country co "
                        + "LEFT JOIN ("
                        + "SELECT CountryCode, SUM(Population) AS CityPopulation "
                        + "FROM city GROUP BY CountryCode"
                        + ") cp ON cp.CountryCode = co.Code "
                        + "GROUP BY co.Region "
                        + "ORDER BY TotalPopulation DESC, Name";

        return getPopulationBreakdowns(query);
    }

    /**
     * Gets city and non-city population totals for each country.
     *
     * @return population breakdowns, largest total population first
     */
    public ArrayList<PopulationBreakdown> getCountryBreakdowns()
    {
        String query =
                "SELECT co.Name AS Name, "
                        + "co.Population AS TotalPopulation, "
                        + "COALESCE(cp.CityPopulation, 0) AS CityPopulation "
                        + "FROM country co "
                        + "LEFT JOIN ("
                        + "SELECT CountryCode, SUM(Population) AS CityPopulation "
                        + "FROM city GROUP BY CountryCode"
                        + ") cp ON cp.CountryCode = co.Code "
                        + "ORDER BY TotalPopulation DESC, Name";

        return getPopulationBreakdowns(query);
    }

    /**
     * Reads breakdown rows. City totals are grouped before the join
     * so each country's population is counted once.
     *
     * @param query SQL query returning name, total and city populations
     * @return population breakdown rows
     */
    private ArrayList<PopulationBreakdown> getPopulationBreakdowns(
            String query)
    {
        ArrayList<PopulationBreakdown> breakdowns = new ArrayList<>();

        if (con == null)
        {
            throw new IllegalStateException("No database connection");
        }

        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rset = stmt.executeQuery())
        {
            while (rset.next())
            {
                PopulationBreakdown breakdown = new PopulationBreakdown();
                breakdown.name = rset.getString("Name");
                breakdown.totalPopulation = rset.getLong("TotalPopulation");
                breakdown.cityPopulation = rset.getLong("CityPopulation");
                breakdowns.add(breakdown);
            }
        }
        catch (java.sql.SQLException e)
        {
            throw new IllegalStateException(
                    "Failed to get population breakdown", e);
        }

        return breakdowns;
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
                    java.util.Locale.UK,
                    "%s | %d | %d | %.2f%% | %d | %.2f%%%n",
                    breakdown.name,
                    breakdown.totalPopulation,
                    breakdown.cityPopulation,
                    breakdown.getCityPercentage(),
                    breakdown.getNonCityPopulation(),
                    breakdown.getNonCityPercentage());
        }
    }
}
