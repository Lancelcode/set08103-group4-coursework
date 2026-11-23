package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the PopulationTotal model.
 */
class PopulationTotalTest
{
    /**
     * The population is a long, so values above Integer.MAX_VALUE fit.
     */
    @Test
    void storesValueLargerThanIntMax()
    {
        PopulationTotal total = new PopulationTotal();

        total.population = 6078749450L;

        assertEquals(6078749450L, total.population);
    }
}