package com.napier.sem;

/**
 * Population breakdown for a continent, region or country.
 *
 * <p>In the World database, the recorded cities of a few countries
 * (Singapore, Gibraltar and Cocos (Keeling) Islands) add up to more people
 * than the country itself, because the figures come from different years.
 * A population cannot be negative, so for those countries the non-city
 * population is shown as 0 and the city percentage as 100% (#118).
 * The recorded city population itself is shown unchanged.</p>
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
     * @return total population minus city population, never below 0
     */
    public long getNonCityPopulation()
    {
        return Math.max(0, totalPopulation - cityPopulation);
    }

    /**
     * Checks whether the recorded cities hold more people than the area.
     *
     * @return true if the city population is larger than the total population
     */
    public boolean citiesExceedTotal()
    {
        return cityPopulation > totalPopulation;
    }

    /**
     * Calculates the city population percentage.
     *
     * @return city percentage, at most 100, or zero when total population is zero
     */
    public double getCityPercentage()
    {
        if (totalPopulation == 0)
        {
            return 0.0;
        }

        return Math.min(100.0, cityPopulation * 100.0 / totalPopulation);
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
