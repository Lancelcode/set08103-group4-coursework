package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.7: Top N cities in a continent (#142).
 * Checks getTopNCitiesByContinent against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCitiesContinentIT extends IntegrationTestBase
{
    /**
     * Europe, N = 2 gives Moscow then London.
     */
    @Test
    void topTwoInEurope()
    {
        ArrayList<City> cities = app.getTopNCitiesByContinent("Europe", 2);

        assertEquals(List.of("Moscow", "London"), names(cities));
        assertLargestFirst(cities);
    }

    /**
     * A continent that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownContinentReturnsNoCities()
    {
        assertTrue(app.getTopNCitiesByContinent("Atlantis", 3).isEmpty());
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCities()
    {
        assertTrue(app.getTopNCitiesByContinent("Europe", 0).isEmpty());
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
