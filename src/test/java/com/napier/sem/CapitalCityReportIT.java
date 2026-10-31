
package com.napier.sem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for capital city reports using the World database.
 */
class CapitalCityReportIT extends IntegrationTestBase
{
    @Test
    void allCapitalsReturns232RowsLargestFirst()
    {
        ArrayList<CapitalCity> capitals = app.getCapitalCities();

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
}

