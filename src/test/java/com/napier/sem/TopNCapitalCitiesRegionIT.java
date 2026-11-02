package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US03.6: Top N capital cities in a region (#151).
 * Checks getTopNCapitalCitiesByRegion against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCapitalCitiesRegionIT extends IntegrationTestBase
{
    /**
     * Caribbean, N = 3 gives the three largest Caribbean capitals, in order.
     */
    @Test
    void topThreeInCaribbean()
    {
        assertEquals(List.of("La Habana", "Santo Domingo de Guzmán", "Port-au-Prince"),
                names(app.getTopNCapitalCitiesByRegion("Caribbean", 3)));
    }

    /**
     * The British Islands have two capitals, so N = 5 gives two rows.
     */
    @Test
    void nLargerThanRegionGivesAll()
    {
        ArrayList<CapitalCity> capitals = app.getTopNCapitalCitiesByRegion("British Islands", 5);

        assertEquals(List.of("London", "Dublin"), names(capitals));
        assertEquals(7285000L, capitals.get(0).population);
        assertEquals(481854L, capitals.get(1).population);
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCapitals()
    {
        assertTrue(app.getTopNCapitalCitiesByRegion("Caribbean", 0).isEmpty());
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
}
