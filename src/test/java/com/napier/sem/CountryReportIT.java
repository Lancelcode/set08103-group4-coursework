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
}