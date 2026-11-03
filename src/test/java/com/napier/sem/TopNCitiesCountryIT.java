package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.9: Top N cities in a country (#144).
 * Checks getTopNCitiesByCountry against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCitiesCountryIT extends IntegrationTestBase
{
    /**
     * United Kingdom, N = 3 gives the three largest UK cities, in order.
     */
    @Test
    void topThreeInUnitedKingdom()
    {
        ArrayList<City> cities = app.getTopNCitiesByCountry("United Kingdom", 3);

        assertEquals(List.of("London", "Birmingham", "Glasgow"), names(cities));
    }

    /**
     * Asking for more cities than the country has gives all 81.
     */
    @Test
    void nLargerThanCountryGivesAll()
    {
        ArrayList<City> cities = app.getTopNCitiesByCountry("United Kingdom", 100);

        assertEquals(81, cities.size());
        assertLargestFirst(cities);
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCities()
    {
        assertTrue(app.getTopNCitiesByCountry("United Kingdom", 0).isEmpty());
    }

    /**
     * Gets the names of the given cities, in order.
     *
     * @param cities cities from a report
     * @return their names
     */
    private static List<String> names(ArrayList<City> cities)
    {
        return cities.stream().map(city -> city.name).toList();
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
}
