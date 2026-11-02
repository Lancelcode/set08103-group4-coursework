package com.napier.sem;

import org.junit.jupiter.api.Test;

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
}