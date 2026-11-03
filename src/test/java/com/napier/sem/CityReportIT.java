package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for city reports using the World database.
 */
class CityReportIT extends IntegrationTestBase
{
    /**
     * Checks that all cities are returned in population order.
     */
    @Test
    void allCitiesInWorldReturns4079RowsLargestFirst()
    {
        ArrayList<City> cities = app.getCities();

        assertNotNull(cities);
        assertEquals(4079, cities.size());

        // Check the first city.
        assertEquals("Mumbai (Bombay)", cities.get(0).name);
        assertEquals("India", cities.get(0).country);
        assertEquals("Maharashtra", cities.get(0).district);
        assertEquals(10500000L, cities.get(0).population);

        // Check the second city.
        assertEquals("Seoul", cities.get(1).name);
        assertEquals("South Korea", cities.get(1).country);
        assertEquals("Seoul", cities.get(1).district);
        assertEquals(9981619L, cities.get(1).population);

        // Check the third city.
        assertEquals("São Paulo", cities.get(2).name);
        assertEquals("Brazil", cities.get(2).country);
        assertEquals("São Paulo", cities.get(2).district);
        assertEquals(9968485L, cities.get(2).population);

        // Every city should have a population greater than
        // or equal to the city below it.
        for (int i = 1; i < cities.size(); i++)
        {
            assertTrue(
                    cities.get(i - 1).population
                            >= cities.get(i).population);
        }
    }

    /**
     * Checks that the city report uses the full country name
     * instead of the country code.
     */
    @Test
    void cityRowShowsCountryNameNotCode()
    {
        ArrayList<City> cities = app.getCities();

        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        City firstCity = cities.get(0);

        assertEquals("Mumbai (Bombay)", firstCity.name);
        assertEquals("India", firstCity.country);
        assertNotEquals("IND", firstCity.country);
    }

    /**
     * Checks that accented city names are read correctly.
     */
    @Test
    void accentedCityNameDisplaysCorrectly()
    {
        ArrayList<City> cities = app.getCities();

        assertEquals("São Paulo", cities.get(2).name);
        assertEquals("São Paulo", cities.get(2).district);
    }
}
