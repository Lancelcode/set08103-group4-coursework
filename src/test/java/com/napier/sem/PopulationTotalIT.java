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

    @Test
    void caribbeanPopulationIs38140000()
    {
        ArrayList<PopulationTotal> totals = app.getPopulationByRegion("Caribbean");

        assertEquals(1, totals.size());
        assertEquals("Caribbean", totals.get(0).name);
        assertEquals(38140000L, totals.get(0).population);
    }

    @Test
    void britishIslandsPopulationIs63398500()
    {
        ArrayList<PopulationTotal> totals = app.getPopulationByRegion("British Islands");

        assertEquals(1, totals.size());
        assertEquals("British Islands", totals.get(0).name);
        assertEquals(63398500L, totals.get(0).population);
    }

    @Test
    void unknownRegionReturnsNoResult()
    {
        assertTrue(app.getPopulationByRegion("Middle Earth").isEmpty());
    }


    @Test
    void unitedKingdomPopulationIs59623400()
    {
        ArrayList<PopulationTotal> totals = app.getPopulationByCountry("United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom", totals.get(0).name);
        assertEquals(59623400L, totals.get(0).population);
    }

    @Test
    void unknownCountryReturnsNoResult()
    {
        assertTrue(app.getPopulationByCountry("Narnia").isEmpty());
    }
}

