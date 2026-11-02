package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.6: Top N cities in the world (#141).
 * Checks getTopNCities against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCitiesWorldIT extends IntegrationTestBase
{
    /**
     * N = 3 gives the three largest cities, in order.
     */
    @Test
    void topThreeCities()
    {
        assertEquals(List.of("Mumbai (Bombay)", "Seoul", "São Paulo"),
                names(app.getTopNCities(3)));
    }

    /**
     * Asking for more cities than exist gives all 4,079.
     */
    @Test
    void nLargerThanCitiesGivesAll()
    {
        ArrayList<City> cities = app.getTopNCities(5000);

        assertEquals(4079, cities.size());
        assertLargestFirst(cities);
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroOrNegativeNGivesNoCities()
    {
        assertTrue(app.getTopNCities(0).isEmpty());
        assertTrue(app.getTopNCities(-5).isEmpty());
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
