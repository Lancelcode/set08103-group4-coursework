
package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import java.io.ByteArrayOutputStream;

import java.io.PrintStream;

class CountryReportTest
{
    private final App app = new App();


    @Test
    void displayCountriesTestNull()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;

        try
        {
            System.setOut(new PrintStream(out));
            assertDoesNotThrow(() -> app.displayCountries(null));
        }
        finally
        {
            System.setOut(original);
        }

        assertEquals("No countries to display", out.toString().trim());
    }




    @Test
    void displayCountriesTestEmpty()
    {
        ArrayList<Country> countries = new ArrayList<>();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;

        try
        {
            System.setOut(new PrintStream(out));
            assertDoesNotThrow(() -> app.displayCountries(countries));
        }
        finally
        {
            System.setOut(original);
        }

        assertEquals(
                "Code | Name | Continent | Region | Population | Capital",
                out.toString().trim());
    }




    @Test
    void displayCountriesTestNullEntry()
    {
        Country country = new Country();
        country.code = "CHN";
        country.name = "China";
        country.continent = "Asia";
        country.region = "Eastern Asia";
        country.population = 1277558000L;
        country.capital = "Peking";

        ArrayList<Country> countries = new ArrayList<>();
        countries.add(null);
        countries.add(country);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;

        try
        {
            System.setOut(new PrintStream(out));
            assertDoesNotThrow(() -> app.displayCountries(countries));
        }
        finally
        {
            System.setOut(original);
        }

        String output = out.toString();

        assertTrue(output.contains(
                "CHN | China | Asia | Eastern Asia | 1277558000 | Peking"));

        assertEquals(2, output.lines().count());
    }


    @Test
    void displayCountriesTestNormal()
    {
        Country country = new Country();
        country.code = "CHN";
        country.name = "China";
        country.continent = "Asia";
        country.region = "Eastern Asia";
        country.population = 1277558000L;
        country.capital = "Peking";

        ArrayList<Country> countries = new ArrayList<>();
        countries.add(country);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;

        try
        {
            System.setOut(new PrintStream(out));
            app.displayCountries(countries);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "CHN | China | Asia | Eastern Asia | 1277558000 | Peking"));
    }


    @Test
    void displayCountriesTestNoCapital()
    {
        Country country = new Country();
        country.code = "ATA";
        country.name = "Antarctica";
        country.continent = "Antarctica";
        country.region = "Antarctica";
        country.population = 0L;
        country.capital = "N/A";

        ArrayList<Country> countries = new ArrayList<>();
        countries.add(country);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;

        try
        {
            System.setOut(new PrintStream(out));
            app.displayCountries(countries);
        }
        finally
        {
            System.setOut(original);
        }

        assertTrue(out.toString().contains(
                "ATA | Antarctica | Antarctica | Antarctica | 0 | N/A"));
    }


}

