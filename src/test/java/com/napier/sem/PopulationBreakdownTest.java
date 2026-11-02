package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests population breakdown calculations without a database.
 */
class PopulationBreakdownTest
{
    /** Tolerance for comparing decimal percentages. */
    private static final double TOLERANCE = 0.000001;

    /**
     * Checks a normal population split.
     */
    @Test
    void calculatesNormalBreakdown()
    {
        PopulationBreakdown breakdown = createBreakdown(1000L, 250L);

        assertEquals(750L, breakdown.getNonCityPopulation());
        assertEquals(25.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(75.0, breakdown.getNonCityPercentage(), TOLERANCE);
    }

    /**
     * Checks that zero population does not cause division by zero.
     */
    @Test
    void handlesZeroPopulation()
    {
        PopulationBreakdown breakdown = createBreakdown(0L, 0L);

        assertEquals(0L, breakdown.getNonCityPopulation());
        assertEquals(0.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(0.0, breakdown.getNonCityPercentage(), TOLERANCE);
    }

    /**
     * Checks an area with no recorded city population.
     */
    @Test
    void handlesNoCityPopulation()
    {
        PopulationBreakdown breakdown = createBreakdown(1000L, 0L);

        assertEquals(1000L, breakdown.getNonCityPopulation());
        assertEquals(0.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(100.0, breakdown.getNonCityPercentage(), TOLERANCE);
    }

    /**
     * Checks an area where everyone lives in recorded cities.
     */
    @Test
    void handlesAllCityPopulation()
    {
        PopulationBreakdown breakdown = createBreakdown(1000L, 1000L);

        assertEquals(0L, breakdown.getNonCityPopulation());
        assertEquals(100.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(0.0, breakdown.getNonCityPercentage(), TOLERANCE);
    }

    /**
     * Checks an area whose recorded cities hold more people than the area,
     * as in Singapore (#118): non-city population 0, city 100%, non-city 0%.
     */
    @Test
    void capsCityPopulationAboveTotal()
    {
        PopulationBreakdown breakdown = createBreakdown(3_567_000L, 4_017_733L);

        assertEquals(0L, breakdown.getNonCityPopulation());
        assertEquals(100.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(0.0, breakdown.getNonCityPercentage(), TOLERANCE);
        assertTrue(breakdown.citiesExceedTotal());
    }

    /**
     * Checks a normal area is not flagged as having too many city people.
     */
    @Test
    void normalAreaIsNotFlagged()
    {
        assertFalse(createBreakdown(1000L, 250L).citiesExceedTotal());
        assertFalse(createBreakdown(1000L, 1000L).citiesExceedTotal());
    }

    /**
     * Checks that percentages retain their fractional part.
     */
    @Test
    void calculatesFractionalPercentages()
    {
        PopulationBreakdown breakdown = createBreakdown(3L, 1L);

        assertEquals(2L, breakdown.getNonCityPopulation());
        assertEquals(
                33.33333333333333,
                breakdown.getCityPercentage(),
                TOLERANCE);
        assertEquals(
                66.66666666666667,
                breakdown.getNonCityPercentage(),
                TOLERANCE);
    }

    /**
     * Checks population values above the integer range.
     */
    @Test
    void handlesLargePopulationTotals()
    {
        PopulationBreakdown breakdown =
                createBreakdown(6_000_000_000L, 2_400_000_000L);

        assertEquals(3_600_000_000L, breakdown.getNonCityPopulation());
        assertEquals(40.0, breakdown.getCityPercentage(), TOLERANCE);
        assertEquals(60.0, breakdown.getNonCityPercentage(), TOLERANCE);
    }

    /**
     * Creates an independent population breakdown for a test.
     *
     * @param total total population
     * @param city population in recorded cities
     * @return the populated test object
     */
    private PopulationBreakdown createBreakdown(long total, long city)
    {
        PopulationBreakdown breakdown = new PopulationBreakdown();
        breakdown.totalPopulation = total;
        breakdown.cityPopulation = city;
        return breakdown;
    }
}