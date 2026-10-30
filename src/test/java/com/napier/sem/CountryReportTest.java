
package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class CountryReportTest
{
    private final App app = new App();

    @Test
    void displayCountriesTestNull()
    {
        assertDoesNotThrow(() -> app.displayCountries(null));
    }


    @Test
    void displayCountriesTestEmpty()
    {
        ArrayList<Country> countries = new ArrayList<>();

        assertDoesNotThrow(() -> app.displayCountries(countries));
    }


    @Test
    void displayCountriesTestNullEntry()
    {
        ArrayList<Country> countries = new ArrayList<>();
        countries.add(null);

        assertDoesNotThrow(() -> app.displayCountries(countries));
    }


}

