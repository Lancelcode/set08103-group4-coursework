package com.napier.sem;

/**
 * Population breakdown for a continent, region or country.
 */
public class PopulationBreakdown
{
    /** Name of the continent, region or country. */
    public String name;

    /** Total population of the area. */
    public long totalPopulation;

    /** Population living in the cities recorded in the database. */
    public long cityPopulation;

    /**
     * Calculates the population outside the recorded cities.
     *
     * @return total population minus city population
     */
    public long getNonCityPopulation()
    {
        return totalPopulation - cityPopulation;
    }

    /**
     * Calculates the city population percentage.
     *
     * @return city percentage, or zero when total population is zero
     */
    public double getCityPercentage()
    {
        if (totalPopulation == 0)
        {
            return 0.0;
        }

        return cityPopulation * 100.0 / totalPopulation;
    }

    /**
     * Calculates the non-city population percentage.
     *
     * @return non-city percentage, or zero when total population is zero
     */
    public double getNonCityPercentage()
    {
        if (totalPopulation == 0)
        {
            return 0.0;
        }

        return getNonCityPopulation() * 100.0 / totalPopulation;
    }
}
