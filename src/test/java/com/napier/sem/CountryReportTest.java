
package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import java.io.ByteArrayOutputStream;

import java.io.PrintStream;

/**
 * Unit tests for the country report display.
 */
class CountryReportTest
{
    /** App used only for its display method. */
    private final App app = new App();


    /**
     * A null list prints a message instead of crashing.
     */
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




    /**
     * An empty list prints the table header only.
     */
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

        assertTrue(squash(out.toString()).contains(
                "| Code | Name | Continent | Region | Population | Capital |"));
        assertEquals(4, out.toString().lines().count());
    }




    /**
     * A null row is skipped and the other rows still print.
     */
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

        assertTrue(squash(output).contains(
                "| CHN | China | Asia | Eastern Asia | 1,277,558,000 | Peking |"));

        assertEquals(5, output.lines().count());
    }


    /**
     * A country prints every column in the right order.
     */
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

        assertTrue(squash(out.toString()).contains(
                "| CHN | China | Asia | Eastern Asia | 1,277,558,000 | Peking |"));
    }


    /**
     * A country with no capital prints N/A.
     */
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

        assertTrue(squash(out.toString()).contains(
                "| ATA | Antarctica | Antarctica | Antarctica | 0 | N/A |"));
    }

    /**
     * Collapses runs of spaces, so table padding does not affect the checks.
     *
     * @param text printed output
     * @return the output with single spaces
     */
    private static String squash(String text)
    {
        return text.replaceAll(" +", " ");
    }
}
