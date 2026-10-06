package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
     * Entry point.
     */
    public static void main(String[] args)
    {
        App app = new App();

        app.connect();

        // Existing country report.
        ArrayList<Country> countries = app.getCountries();
        app.displayCountries(countries);

        // Issue #14 - all cities in the world.
        ArrayList<City> cities = app.getCities();
        app.displayCities(cities);

        app.disconnect();
    }

    /**
     * Connects to the World database.
     * DB_HOST is used when running through Docker/CI.
     * localhost is used when running locally.
     */
    public void connect()
    {
        String host = System.getenv("DB_HOST");

        if (host == null || host.isEmpty())
        {
            host = "localhost";
        }

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://" + host
                            + ":3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "root");

            System.out.println("Successfully connected to database");
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to database");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets all countries in the world ordered by population.
     *
     * @return list of countries
     */
    public ArrayList<Country> getCountries()
    {
        ArrayList<Country> countries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No database connection");
            return countries;
        }

        String query =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, "
                        + "COALESCE(ci.Name, 'N/A') AS Capital "
                        + "FROM country c "
                        + "LEFT JOIN city ci ON c.Capital = ci.ID "
                        + "ORDER BY c.Population DESC";

        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(query))
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
        catch (Exception e)
        {
            System.out.println("Failed to get countries");
            System.out.println(e.getMessage());
        }

        return countries;
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
}