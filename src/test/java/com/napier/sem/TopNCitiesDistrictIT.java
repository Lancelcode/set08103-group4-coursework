package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.10: Top N cities in a district (#145).
 * Checks getTopNCitiesByDistrict against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCitiesDistrictIT extends IntegrationTestBase
{
    /**
     * Scotland, N = 2 gives Glasgow then Edinburgh.
     */
    @Test
    void topTwoInScotland()
    {
        assertEquals(List.of("Glasgow", "Edinburgh"),
                names(app.getTopNCitiesByDistrict("Scotland", 2)));
    }

    /**
     * Scotland has only four recorded cities, so N = 10 gives four rows.
     */
    @Test
    void nLargerThanDistrictGivesAll()
    {
        ArrayList<City> cities = app.getTopNCitiesByDistrict("Scotland", 10);

        assertEquals(List.of("Glasgow", "Edinburgh", "Aberdeen", "Dundee"), names(cities));
        assertLargestFirst(cities);
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCities()
    {
        assertTrue(app.getTopNCitiesByDistrict("Scotland", 0).isEmpty());
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
