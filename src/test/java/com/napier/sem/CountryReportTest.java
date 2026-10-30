
package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountryReportTest
{
    private final App app = new App();

    @Test
    void displayCountriesTestNull()
    {
        assertDoesNotThrow(() -> app.displayCountries(null));
    }
}

