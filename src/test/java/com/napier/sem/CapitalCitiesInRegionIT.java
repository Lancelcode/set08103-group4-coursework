package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US03.3: All capital cities in a region, largest population first (#148).
 * Checks getCapitalCitiesByRegion against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CapitalCitiesInRegionIT extends IntegrationTestBase
{
    /**
     * The Caribbean has 24 capital cities.
     */
    @Test
    void caribbeanHas24Capitals()
    {
        assertEquals(24, app.getCapitalCitiesByRegion("Caribbean").size());
    }

    /**
     * The largest Caribbean capitals come first.
     */
    @Test
    void caribbeanIsLargestFirst()
    {
        ArrayList<CapitalCity> capitals = app.getCapitalCitiesByRegion("Caribbean");

        assertEquals(List.of("La Habana", "Santo Domingo de Guzmán", "Port-au-Prince"),
                names(capitals).subList(0, 3));
        assertEquals(2256000L, capitals.get(0).population);
        assertEquals(1609966L, capitals.get(1).population);
        assertEquals(884472L, capitals.get(2).population);
        assertLargestFirst(capitals);
    }

    /**
     * A region that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownRegionReturnsNoCapitals()
    {
        assertTrue(app.getCapitalCitiesByRegion("Middle Earth").isEmpty());
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
