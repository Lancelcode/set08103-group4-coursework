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



    @Test
    void scotlandPopulationIs1429620()
    {
        ArrayList<PopulationTotal> totals =
                app.getPopulationByDistrict("Scotland", "United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom - Scotland", totals.get(0).name);
        assertEquals(1429620L, totals.get(0).population);
    }

    @Test
    void distritoFederalPopulationIsInMexico()
    {
        ArrayList<PopulationTotal> totals =
                app.getPopulationByDistrict("Distrito Federal", "Mexico");

        assertEquals(1, totals.size());
        assertEquals("Mexico - Distrito Federal", totals.get(0).name);
        assertTrue(totals.get(0).population > 0);
    }

    @Test
    void scotlandInFranceReturnsNoResult()
    {
        assertTrue(app.getPopulationByDistrict("Scotland", "France").isEmpty());
    }



    @Test
    void edinburghPopulationIs450180()
    {
        ArrayList<PopulationTotal> totals =
                app.getPopulationByCity("Edinburgh", "United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom - Edinburgh (Scotland)", totals.get(0).name);
        assertEquals(450180L, totals.get(0).population);
    }

    @Test
    void hamiltonCanadaPopulationIs335614()
    {
        ArrayList<PopulationTotal> totals =
                app.getPopulationByCity("Hamilton", "Canada");

        assertEquals(1, totals.size());
        assertEquals("Canada - Hamilton (Ontario)", totals.get(0).name);
        assertEquals(335614L, totals.get(0).population);
    }

    @Test
    void richmondUnitedStatesReturnsTwoCities()
    {
        ArrayList<PopulationTotal> totals =
                app.getPopulationByCity("Richmond", "United States");

        assertEquals(2, totals.size());

        assertTrue(totals.stream().anyMatch(city ->
                city.population == 197790L));

        assertTrue(totals.stream().anyMatch(city ->
                city.population == 94100L));
    }

    @Test
    void edinburghInFranceReturnsNoResult()
    {
        assertTrue(app.getPopulationByCity("Edinburgh", "France").isEmpty());
    }
}

