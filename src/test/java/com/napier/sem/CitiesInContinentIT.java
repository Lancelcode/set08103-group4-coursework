package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.2: All cities in a continent, largest population first (#137).
 * Checks getCitiesByContinent against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CitiesInContinentIT extends IntegrationTestBase
{
    /**
     * Europe has 841 cities.
     */
    @Test
    void europeHas841Cities()
    {
        assertEquals(841, app.getCitiesByContinent("Europe").size());
    }

    /**
     * Moscow and London are the largest European cities.
     */
    @Test
    void europeIsLargestFirst()
    {
        ArrayList<City> cities = app.getCitiesByContinent("Europe");

        assertCity(cities.get(0), "Moscow", "Russian Federation", "Moscow (City)", 8389200L);
        assertCity(cities.get(1), "London", "United Kingdom", "England", 7285000L);
        assertLargestFirst(cities);
    }

    /**
     * A continent that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownContinentReturnsNoCities()
    {
        assertTrue(app.getCitiesByContinent("Atlantis").isEmpty());
    }

    /**
     * Checks every city has a population at least as large as the next one.
     *
     * @param cities cities from a report
     */
    private static void assertLargestFirst(ArrayList<City> cities)
    {
        for (int i = 1; i < cities.size(); i++)
        {
            assertTrue(cities.get(i - 1).population >= cities.get(i).population,
                    "Row " + i + " is larger than the row above it");
        }
    }

    /**
     * Checks one city row has the expected values.
     *
     * @param city row to check
     * @param name expected city name
     * @param country expected country name
     * @param district expected district
     * @param population expected population
     */
    private static void assertCity(City city, String name, String country,
                                   String district, long population)
    {
        assertEquals(name, city.name);
        assertEquals(country, city.country);
        assertEquals(district, city.district);
        assertEquals(population, city.population);
    }
}
