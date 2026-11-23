package com.napier.sem;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Integration tests for population lookups
 * using the World database.
 */
class PopulationTotalIT extends IntegrationTestBase
{
    /**
     * Population of the world.
     */
    @Test
    void worldPopulationIs6078749450()
    {
        assertEquals(6078749450L, app.getWorldPopulation());
    }

    /**
     * Population of Europe.
     */
    @Test
    void europePopulationIs730074600()
    {
        List<PopulationTotal> totals = app.getPopulationByContinent("Europe");

        assertEquals(1, totals.size());
        assertEquals("Europe", totals.get(0).name);
        assertEquals(730074600L, totals.get(0).population);
    }

    /**
     * An unknown continent returns no result.
     */
    @Test
    void unknownContinentReturnsNoResult()
    {
        assertTrue(app.getPopulationByContinent("Atlantis").isEmpty());
    }

    /**
     * Population of the Caribbean.
     */
    @Test
    void caribbeanPopulationIs38140000()
    {
        List<PopulationTotal> totals = app.getPopulationByRegion("Caribbean");

        assertEquals(1, totals.size());
        assertEquals("Caribbean", totals.get(0).name);
        assertEquals(38140000L, totals.get(0).population);
    }

    /**
     * Population of the British Islands.
     */
    @Test
    void britishIslandsPopulationIs63398500()
    {
        List<PopulationTotal> totals = app.getPopulationByRegion("British Islands");

        assertEquals(1, totals.size());
        assertEquals("British Islands", totals.get(0).name);
        assertEquals(63398500L, totals.get(0).population);
    }

    /**
     * An unknown region returns no result.
     */
    @Test
    void unknownRegionReturnsNoResult()
    {
        assertTrue(app.getPopulationByRegion("Middle Earth").isEmpty());
    }


    /**
     * Population of the United Kingdom.
     */
    @Test
    void unitedKingdomPopulationIs59623400()
    {
        List<PopulationTotal> totals = app.getPopulationByCountry("United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom", totals.get(0).name);
        assertEquals(59623400L, totals.get(0).population);
    }

    /**
     * An unknown country returns no result.
     */
    @Test
    void unknownCountryReturnsNoResult()
    {
        assertTrue(app.getPopulationByCountry("Narnia").isEmpty());
    }



    /**
     * Population of Scotland, from its recorded cities.
     */
    @Test
    void scotlandPopulationIs1429620()
    {
        List<PopulationTotal> totals =
                app.getPopulationByDistrict("Scotland", "United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom - Scotland", totals.get(0).name);
        assertEquals(1429620L, totals.get(0).population);
    }

    /**
     * Distrito Federal is looked up in the right country.
     */
    @Test
    void distritoFederalPopulationIsInMexico()
    {
        List<PopulationTotal> totals =
                app.getPopulationByDistrict("Distrito Federal", "Mexico");

        assertEquals(1, totals.size());
        assertEquals("Mexico - Distrito Federal", totals.get(0).name);
        assertTrue(totals.get(0).population > 0);
    }

    /**
     * A district in the wrong country returns no result.
     */
    @Test
    void scotlandInFranceReturnsNoResult()
    {
        assertTrue(app.getPopulationByDistrict("Scotland", "France").isEmpty());
    }



    /**
     * Population of Edinburgh.
     */
    @Test
    void edinburghPopulationIs450180()
    {
        List<PopulationTotal> totals =
                app.getPopulationByCity("Edinburgh", "United Kingdom");

        assertEquals(1, totals.size());
        assertEquals("United Kingdom - Edinburgh (Scotland)", totals.get(0).name);
        assertEquals(450180L, totals.get(0).population);
    }

    /**
     * Hamilton in Canada, not in another country.
     */
    @Test
    void hamiltonCanadaPopulationIs335614()
    {
        List<PopulationTotal> totals =
                app.getPopulationByCity("Hamilton", "Canada");

        assertEquals(1, totals.size());
        assertEquals("Canada - Hamilton (Ontario)", totals.get(0).name);
        assertEquals(335614L, totals.get(0).population);
    }

    /**
     * Richmond in the United States matches two cities.
     */
    @Test
    void richmondUnitedStatesReturnsTwoCities()
    {
        List<PopulationTotal> totals =
                app.getPopulationByCity("Richmond", "United States");

        assertEquals(2, totals.size());

        assertTrue(totals.stream().anyMatch(city ->
                city.population == 197790L));

        assertTrue(totals.stream().anyMatch(city ->
                city.population == 94100L));
    }

    /**
     * A city in the wrong country returns no result.
     */
    @Test
    void edinburghInFranceReturnsNoResult()
    {
        assertTrue(app.getPopulationByCity("Edinburgh", "France").isEmpty());
    }
}

