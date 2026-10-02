package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    private Connection con = null;

    public static void main(String[] args)
    {
        App app = new App();

        app.connect();

        ArrayList<Country> countries = app.getCountries();

        app.displayCountries(countries);

        app.disconnect();
    }

    public void connect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "root"
            );

            System.out.println("Successfully connected to database");
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to database");
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Country> getCountries()
    {
        ArrayList<Country> countries = new ArrayList<>();

        try
        {
            Statement stmt = con.createStatement();

            String query =
                    "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, " +
                            "COALESCE(ci.Name, 'N/A') AS Capital " +
                            "FROM country c " +
                            "LEFT JOIN city ci ON c.Capital = ci.ID " +
                            "ORDER BY c.Population DESC";

            ResultSet rset = stmt.executeQuery(query);

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

    public void displayCountries(ArrayList<Country> countries)
    {
        System.out.println("Code | Name | Continent | Region | Population | Capital");

        for (Country country : countries)
        {
            System.out.println(
                    country.code + " | " +
                            country.name + " | " +
                            country.continent + " | " +
                            country.region + " | " +
                            country.population + " | " +
                            country.capital
            );
        }
    }

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