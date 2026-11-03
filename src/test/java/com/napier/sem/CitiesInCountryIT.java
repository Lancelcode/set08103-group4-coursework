package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.4: All cities in a country, largest population first (#139).
 * Checks getCitiesByCountry against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CitiesInCountryIT extends IntegrationTestBase
{
    /**
     * The United Kingdom has 81 cities and every row is in the United Kingdom.
     */
    @Test
    void unitedKingdomHas81Cities()
    {
        ArrayList<City> cities = app.getCitiesByCountry("United Kingdom");

        assertEquals(81, cities.size());
        for (City city : cities)
        {
            assertEquals("United Kingdom", city.country);
        }
    }

    /**
     * London, Birmingham and Glasgow are the largest UK cities.
     */
    @Test
    void unitedKingdomIsLargestFirst()
    {
        ArrayList<City> cities = app.getCitiesByCountry("United Kingdom");

        assertCity(cities.get(0), "London", "United Kingdom", "England", 7285000L);
        assertCity(cities.get(1), "Birmingham", "United Kingdom", "England", 1013000L);
        assertCity(cities.get(2), "Glasgow", "United Kingdom", "Scotland", 619680L);
        assertLargestFirst(cities);
    }

    /**
     * A country that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownCountryReturnsNoCities()
    {
        assertTrue(app.getCitiesByCountry("Narnia").isEmpty());
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
