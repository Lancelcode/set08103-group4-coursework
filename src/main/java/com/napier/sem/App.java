package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Population reporting application. Connects to the MySQL world database
 * and prints reports.
 */
public class App
{
    /** Database connection, null until connect() succeeds. */
    private Connection con = null;

    /**
     * Entry point. Connects, prints the country report, then disconnects.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        App app = new App();

        app.connect();

        System.out.println("World Population: " + app.getWorldPopulation());

        System.out.println("\nPopulation by Continent:");
        ArrayList<PopulationTotal> continents = app.getContinentPopulations();
        for (PopulationTotal total : continents)
        {
            System.out.println(total.name + " | " + total.population);
        }

        System.out.println("\nPopulation by Region:");
        ArrayList<PopulationTotal> regions = app.getRegionPopulations();
        for (PopulationTotal total : regions)
        {
            System.out.println(total.name + " | " + total.population);
        }

        System.out.println("\nPopulation by District:");
        ArrayList<PopulationTotal> districts = app.getDistrictPopulations();
        for (PopulationTotal total : districts)
        {
            System.out.println(total.name + " | " + total.population);
        }

        System.out.println("\nPopulation by Country:");
        ArrayList<Country> countries = app.getCountries();
        app.displayCountries(countries);

        System.out.println("\nPopulation by City:");
        ArrayList<PopulationTotal> cities = app.getCityPopulations();
        for (PopulationTotal total : cities)
        {
            System.out.println(total.name + " | " + total.population);
        }

        app.disconnect();
    }

    /**
     * Connects to the world database. The host comes from the DB_HOST
     * environment variable and defaults to localhost.
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
                    "jdbc:mysql://" + host + ":3306/world?allowPublicKeyRetrieval=true&useSSL=false",
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
     * Gets all countries in the world, largest population first.
     * A country with no recorded capital gets the capital "N/A".
     *
     * @return the countries, or an empty list if the query fails
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
     * Prints a country report to the console.
     *
     * @param countries the countries to print
     */
    public void displayCountries(ArrayList<Country> countries)
    {
        System.out.println("Code | Name | Continent | Region | Population | Capital");

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
     * Closes the database connection if one is open.
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
            System.out.println("Error closing database connection");
        }
    }
}