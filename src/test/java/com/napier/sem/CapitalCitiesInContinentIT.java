package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US03.2: All capital cities in a continent, largest population first (#147).
 * Checks getCapitalCitiesByContinent against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CapitalCitiesInContinentIT extends IntegrationTestBase
{
    /**
     * Europe has 46 capital cities.
     */
    @Test
    void europeHas46Capitals()
    {
        assertEquals(46, app.getCapitalCitiesByContinent("Europe").size());
    }

    /**
     * Moscow, London and Berlin are the largest European capitals.
     */
    @Test
    void europeIsLargestFirst()
    {
        ArrayList<CapitalCity> capitals = app.getCapitalCitiesByContinent("Europe");

        assertEquals(List.of("Moscow", "London", "Berlin"), names(capitals).subList(0, 3));
        assertEquals(8389200L, capitals.get(0).population);
        assertEquals(7285000L, capitals.get(1).population);
        assertEquals(3386667L, capitals.get(2).population);
        assertLargestFirst(capitals);
    }

    /**
     * A continent that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownContinentReturnsNoCapitals()
    {
        assertTrue(app.getCapitalCitiesByContinent("Atlantis").isEmpty());
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
