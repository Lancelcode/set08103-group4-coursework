package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CityReportTest
{
    /**
     * Checks that displayCities can handle a null list.
     */
    @Test
    void displayCitiesTestNull()
    {
        App app = new App();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCities(null);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("No cities to display"));
    }

    /**
     * Checks that an empty list only prints the header.
     */
    @Test
    void displayCitiesTestEmpty()
    {
        App app = new App();
        ArrayList<City> cities = new ArrayList<>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCities(cities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("Name | Country | District | Population"));
    }

    /**
     * Checks that a null city inside the list is skipped.
     */
    @Test
    void displayCitiesTestNullEntry()
    {
        App app = new App();
        ArrayList<City> cities = new ArrayList<>();

        cities.add(null);

        City city = new City();
        city.name = "Edinburgh";
        city.country = "United Kingdom";
        city.district = "Scotland";
        city.population = 450180;

        cities.add(city);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCities(cities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "Edinburgh | United Kingdom | Scotland | 450180"));
    }

    /**
     * Checks that a normal city is displayed correctly.
     */
    @Test
    void displayCitiesTestNormal()
    {
        App app = new App();
        ArrayList<City> cities = new ArrayList<>();

        City city = new City();
        city.name = "Edinburgh";
        city.country = "United Kingdom";
        city.district = "Scotland";
        city.population = 450180;

        cities.add(city);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try
        {
            app.displayCities(cities);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "Edinburgh | United Kingdom | Scotland | 450180"));
    }
}