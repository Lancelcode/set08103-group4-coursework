package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.3: All cities in a region, largest population first (#138).
 * Checks getCitiesByRegion against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CitiesInRegionIT extends IntegrationTestBase
{
    /**
     * The Caribbean has 58 cities.
     */
    @Test
    void caribbeanHas58Cities()
    {
        assertEquals(58, app.getCitiesByRegion("Caribbean").size());
    }

    /**
     * The largest Caribbean cities come first.
     */
    @Test
    void caribbeanIsLargestFirst()
    {
        ArrayList<City> cities = app.getCitiesByRegion("Caribbean");

        assertCity(cities.get(0), "La Habana", "Cuba", "La Habana", 2256000L);
        assertCity(cities.get(1), "Santo Domingo de Guzmán", "Dominican Republic",
                "Distrito Nacional", 1609966L);
        assertCity(cities.get(2), "Port-au-Prince", "Haiti", "Ouest", 884472L);
        assertLargestFirst(cities);
    }

    /**
     * A region that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownRegionReturnsNoCities()
    {
        assertTrue(app.getCitiesByRegion("Middle Earth").isEmpty());
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
