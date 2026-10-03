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
        ArrayList<Country> countries = app.getCountries();
        app.displayCountries(countries);
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