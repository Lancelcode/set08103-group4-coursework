package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US03.4: Top N capital cities in the world (#149).
 * Checks getTopNCapitalCities against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCapitalCitiesWorldIT extends IntegrationTestBase
{
    /**
     * N = 3 gives the three largest capitals, in order.
     */
    @Test
    void topThreeCapitals()
    {
        assertEquals(List.of("Seoul", "Jakarta", "Ciudad de México"),
                names(app.getTopNCapitalCities(3)));
    }

    /**
     * Asking for more capitals than exist gives all 232.
     */
    @Test
    void nLargerThanCapitalsGivesAll()
    {
        ArrayList<CapitalCity> capitals = app.getTopNCapitalCities(500);

        assertEquals(232, capitals.size());
        assertLargestFirst(capitals);
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroOrNegativeNGivesNoCapitals()
    {
        assertTrue(app.getTopNCapitalCities(0).isEmpty());
        assertTrue(app.getTopNCapitalCities(-5).isEmpty());
    }

    /**
     * Gets the names of the given capital cities, in order.
     *
     * @param capitals capital cities from a report
     * @return their names
     */
    private static List<String> names(ArrayList<CapitalCity> capitals)
    {
        return capitals.stream().map(capital -> capital.name).toList();
    }

    /**
     * Checks every capital has a population at least as large as the next one.
     *
     * @param capitals capital cities from a report
     */
    private static void assertLargestFirst(ArrayList<CapitalCity> capitals)
    {
        for (int i = 1; i < capitals.size(); i++)
        {
            assertTrue(capitals.get(i - 1).population >= capitals.get(i).population,
                    "Row " + i + " is larger than the row above it");
        }
    }
}
