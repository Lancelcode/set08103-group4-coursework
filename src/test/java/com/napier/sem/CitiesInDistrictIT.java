package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US02.5: All cities in a district, largest population first (#140).
 * Checks getCitiesByDistrict against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class CitiesInDistrictIT extends IntegrationTestBase
{
    /**
     * Scotland has exactly four recorded cities.
     */
    @Test
    void scotlandHasFourCities()
    {
        ArrayList<City> cities = app.getCitiesByDistrict("Scotland");

        assertEquals(4, cities.size());
        assertCity(cities.get(0), "Glasgow", "United Kingdom", "Scotland", 619680L);
        assertCity(cities.get(1), "Edinburgh", "United Kingdom", "Scotland", 450180L);
        assertCity(cities.get(2), "Aberdeen", "United Kingdom", "Scotland", 213070L);
        assertCity(cities.get(3), "Dundee", "United Kingdom", "Scotland", 146690L);
    }

    /**
     * A district name used in several countries lists all of them,
     * and the Country column tells them apart.
     */
    @Test
    void sharedDistrictNameListsEveryCountry()
    {
        ArrayList<City> cities = app.getCitiesByDistrict("Distrito Federal");

        assertEquals(5, cities.size());
        assertCity(cities.get(0), "Ciudad de México", "Mexico", "Distrito Federal", 8591309L);
        assertEquals(Set.of("Mexico", "Argentina", "Venezuela", "Brazil"),
                cities.stream().map(city -> city.country).collect(Collectors.toSet()));
        assertLargestFirst(cities);
    }

    /**
     * A district that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownDistrictReturnsNoCities()
    {
        assertTrue(app.getCitiesByDistrict("Atlantis").isEmpty());
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
