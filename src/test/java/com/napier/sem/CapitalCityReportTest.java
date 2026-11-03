package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CapitalCityReportTest
{
    /**
     * Checks that displayCapitalCities can handle a null list.
     */
    @Test
    void displayCapitalCitiesTestNull()
    {
        App app = new App();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCapitalCities(null);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("No capital cities to display"));
    }

    /**
     * Checks that an empty list only prints the header.
     */
    @Test
    void displayCapitalCitiesTestEmpty()
    {
        App app = new App();
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCapitalCities(capitalCities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("Name | Country | Population"));
    }

    /**
     * Checks that a null entry inside the list is skipped.
     */
    @Test
    void displayCapitalCitiesTestNullEntry()
    {
        App app = new App();
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        capitalCities.add(null);

        CapitalCity capitalCity = new CapitalCity();
        capitalCity.name = "London";
        capitalCity.country = "United Kingdom";
        capitalCity.population = 7285000;

        capitalCities.add(capitalCity);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCapitalCities(capitalCities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "London | United Kingdom | 7285000"));
    }

    /**
     * Checks that a normal capital city is displayed correctly.
     */
    @Test
    void displayCapitalCitiesTestNormal()
    {
        App app = new App();
        ArrayList<CapitalCity> capitalCities = new ArrayList<>();

        CapitalCity capitalCity = new CapitalCity();
        capitalCity.name = "London";
        capitalCity.country = "United Kingdom";
        capitalCity.population = 7285000;

        capitalCities.add(capitalCity);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCapitalCities(capitalCities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "London | United Kingdom | 7285000"));
    }
}