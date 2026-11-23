
package com.napier.sem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for capital city reports using the World database.
 */
class CapitalCityReportIT extends IntegrationTestBase
{
    /**
     * All capital cities in the world: 232 rows, largest first.
     */
    @Test
    void allCapitalsReturns232RowsLargestFirst()
    {
        List<CapitalCity> capitals = app.getCapitalCities();

        assertEquals(232, capitals.size());

        assertEquals("Seoul", capitals.get(0).name);
        assertEquals("South Korea", capitals.get(0).country);
        assertEquals(9981619L, capitals.get(0).population);

        assertEquals("Jakarta", capitals.get(1).name);
        assertEquals("Indonesia", capitals.get(1).country);
        assertEquals(9604900L, capitals.get(1).population);

        assertEquals("Ciudad de México", capitals.get(2).name);
        assertEquals("Mexico", capitals.get(2).country);
        assertEquals(8591309L, capitals.get(2).population);

        for (int i = 1; i < capitals.size(); i++)
        {
            assertTrue(capitals.get(i - 1).population >= capitals.get(i).population);
        }
    }

    /**
     * The capital is matched by city ID, not by name.
     */
    @Test
    void capitalFoundByIdNotName()
    {
        List<CapitalCity> capitals = app.getCapitalCities();

        for (CapitalCity capital : capitals)
        {
            assertFalse(
                    capital.name.equals("London")
                            && capital.country.equals("Canada")
            );
        }
    }

    /**
     * Top 2 capital cities in Europe.
     */
    @Test
    void topTwoCapitalsInEurope()
    {
        List<CapitalCity> capitals =
                app.getTopNCapitalCitiesByContinent("Europe", 2);

        assertEquals(2, capitals.size());

        assertEquals("Moscow", capitals.get(0).name);
        assertEquals("London", capitals.get(1).name);
    }

    /**
     * Top N capital cities in an unknown continent returns an empty list.
     */
    @Test
    void topNCapitalsInUnknownContinentReturnsEmpty()
    {
        List<CapitalCity> capitals =
                app.getTopNCapitalCitiesByContinent("Atlantis", 3);

        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());
    }

    /**
     * Top 3 capital cities in the Caribbean.
     */
    @Test
    void topThreeCapitalsInCaribbean()
    {
        List<CapitalCity> capitals =
                app.getTopNCapitalCitiesByRegion("Caribbean", 3);

        assertEquals(3, capitals.size());

        assertEquals("La Habana", capitals.get(0).name);
        assertEquals("Santo Domingo de Guzmán", capitals.get(1).name);
        assertEquals("Port-au-Prince", capitals.get(2).name);
    }

    /**
     * N larger than the number of capitals in a region returns them all.
     */
    @Test
    void topNLargerThanRegionReturnsAllCapitals()
    {
        List<CapitalCity> capitals =
                app.getTopNCapitalCitiesByRegion("British Islands", 5);

        assertEquals(2, capitals.size());

        assertEquals("London", capitals.get(0).name);
        assertEquals(7285000L, capitals.get(0).population);

        assertEquals("Dublin", capitals.get(1).name);
        assertEquals(481854L, capitals.get(1).population);
    }

    /**
     * Top 3 capital cities in the world.
     */
    @Test
    void topThreeCapitalsInWorld()
    {
        List<CapitalCity> capitals = app.getTopNCapitalCities(3);

        assertEquals(3, capitals.size());

        assertEquals("Seoul", capitals.get(0).name);
        assertEquals("Jakarta", capitals.get(1).name);
        assertEquals("Ciudad de México", capitals.get(2).name);
    }

    /**
     * N larger than the number of capitals in the world returns them all.
     */
    @Test
    void topNLargerThanCapitalCountReturnsAll()
    {
        List<CapitalCity> capitals = app.getTopNCapitalCities(500);

        assertEquals(232, capitals.size());

        assertEquals("Seoul", capitals.get(0).name);
        assertEquals("Jakarta", capitals.get(1).name);
        assertEquals("Ciudad de México", capitals.get(2).name);
    }
}
