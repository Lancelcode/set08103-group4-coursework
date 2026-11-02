package com.napier.sem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for population lookups
 * using the World database.
 */
class PopulationTotalIT extends IntegrationTestBase
{
    @Test
    void worldPopulationIs6078749450()
    {
        assertEquals(6078749450L, app.getWorldPopulation());
    }

    @Test
    void europePopulationIs730074600()
    {
        ArrayList<PopulationTotal> totals = app.getPopulationByContinent("Europe");

        assertEquals(1, totals.size());
        assertEquals("Europe", totals.get(0).name);
        assertEquals(730074600L, totals.get(0).population);
    }

    @Test
    void unknownContinentReturnsNoResult()
    {
        assertTrue(app.getPopulationByContinent("Atlantis").isEmpty());
    }
}

