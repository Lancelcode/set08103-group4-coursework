package com.napier.sem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class CountryReportIT extends IntegrationTestBase
{
    @Test
    void testAllCountries()
    {
        ArrayList<Country> countries = app.getCountries();

        assertNotNull(countries);
        assertEquals(239, countries.size());

        assertEquals("China", countries.get(0).name);
        assertEquals(1277558000L, countries.get(0).population);
        assertEquals("Peking", countries.get(0).capital);

        assertEquals("India", countries.get(1).name);
        assertEquals("United States", countries.get(2).name);

        for (int i = 1; i < countries.size(); i++)
        {
            assertTrue(countries.get(i - 1).population >= countries.get(i).population);
        }
    }

    @Test
    void testAntarcticaCapital()
    {
        ArrayList<Country> countries = app.getCountries();

        Country antarctica = countries.stream()
                .filter(c -> c.code.equals("ATA"))
                .findFirst()
                .orElse(null);

        assertNotNull(antarctica);
        assertEquals("N/A", antarctica.capital);
    }

    @Test
    void topThreeCountriesInCaribbean()
    {
        ArrayList<Country> countries = app.getTopNCountriesByRegion("Caribbean", 3);

        assertEquals(3, countries.size());
        assertEquals("Cuba", countries.get(0).name);
        assertEquals("Dominican Republic", countries.get(1).name);
        assertEquals("Haiti", countries.get(2).name);
    }

    @Test
    void topTenCountriesInBritishIslands()
    {
        ArrayList<Country> countries = app.getTopNCountriesByRegion("British Islands", 10);

        assertEquals(2, countries.size());
        assertEquals("United Kingdom", countries.get(0).name);
        assertEquals("Ireland", countries.get(1).name);
    }

    @Test
    void topThreeCountriesInEurope()
    {
        ArrayList<Country> countries = app.getTopNCountriesByContinent("Europe", 3);

        assertEquals(3, countries.size());
        assertEquals("Russian Federation", countries.get(0).name);
        assertEquals("Germany", countries.get(1).name);
        assertEquals("United Kingdom", countries.get(2).name);
    }

    @Test
    void top100CountriesInEuropeReturns46Rows()
    {
        ArrayList<Country> countries = app.getTopNCountriesByContinent("Europe", 100);

        assertNotNull(countries);
        assertEquals(46, countries.size());

        for (Country country : countries)
        {
            assertEquals("Europe", country.continent);
        }
    }

    @Test
    void topFiveCountriesInWorld()
    {
        ArrayList<Country> countries = app.getTopNCountries(5);

        assertEquals(5, countries.size());
        assertEquals("China", countries.get(0).name);
        assertEquals("India", countries.get(1).name);
        assertEquals("United States", countries.get(2).name);
        assertEquals("Indonesia", countries.get(3).name);
        assertEquals("Brazil", countries.get(4).name);
    }

    @Test
    void top300CountriesInWorldReturns239Rows()
    {
        ArrayList<Country> countries = app.getTopNCountries(300);

        assertEquals(239, countries.size());
    }

    @Test
    void topZeroCountriesInWorldReturnsEmptyList()
    {
        ArrayList<Country> countries = app.getTopNCountries(0);

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void countriesInCaribbeanReturns24RowsLargestFirst()
    {
        ArrayList<Country> countries = app.getCountriesByRegion("Caribbean");

        assertEquals(24, countries.size());
        assertEquals("Cuba", countries.get(0).name);
        assertEquals("Dominican Republic", countries.get(1).name);
        assertEquals("Haiti", countries.get(2).name);

        for (Country country : countries)
        {
            assertEquals("Caribbean", country.region);
        }
    }

    @Test
    void countriesInEuropeReturns46RowsLargestFirst()
    {
        ArrayList<Country> countries = app.getCountriesByContinent("Europe");

        assertEquals(46, countries.size());
        assertEquals("Russian Federation", countries.get(0).name);
        assertEquals("Germany", countries.get(1).name);
        assertEquals("United Kingdom", countries.get(2).name);

        for (Country country : countries)
        {
            assertEquals("Europe", country.continent);
        }

        for (int i = 1; i < countries.size(); i++)
        {
            assertTrue(countries.get(i - 1).population >= countries.get(i).population);
        }
    }

    @Test
    void unknownRegionReturnsEmptyList()
    {
        ArrayList<Country> countries = app.getCountriesByRegion("Atlantis");

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }

    @Test
    void unknownContinentReturnsEmptyList()
    {
        ArrayList<Country> countries = app.getCountriesByContinent("Atlantis");

        assertNotNull(countries);
        assertTrue(countries.isEmpty());
    }
}
