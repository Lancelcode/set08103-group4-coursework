package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.8: Top N cities in a region (#143).
 * Checks getTopNCitiesByRegion against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCitiesRegionIT extends IntegrationTestBase
{
    /**
     * Caribbean, N = 3 gives the three largest Caribbean cities, in order.
     */
    @Test
    void topThreeInCaribbean()
    {
        ArrayList<City> cities = app.getTopNCitiesByRegion("Caribbean", 3);

        assertEquals(List.of("La Habana", "Santo Domingo de Guzmán", "Port-au-Prince"),
                names(cities));
    }

    /**
     * A region that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownRegionReturnsNoCities()
    {
        assertTrue(app.getTopNCitiesByRegion("Middle Earth", 3).isEmpty());
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCities()
    {
        assertTrue(app.getTopNCitiesByRegion("Caribbean", 0).isEmpty());
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
}
