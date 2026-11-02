package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PopulationTotalTest
{
    @Test
    void storesValueLargerThanIntMax()
    {
        PopulationTotal total = new PopulationTotal();

        total.population = 6078749450L;

        assertEquals(6078749450L, total.population);
    }
}