package com.napier.sem;

/**
 * Population breakdown for a continent, region or country.
 */
public class PopulationBreakdown
{
    public String name;
    public long totalPopulation;
    public long cityPopulation;

    public long getNonCityPopulation()
    {
        return totalPopulation - cityPopulation;
    }

    public double getCityPercentage()
    {
        if (totalPopulation == 0)
        {
            return 0.0;
        }

        return cityPopulation * 100.0 / totalPopulation;
    }

    public double getNonCityPercentage()
    {
        if (totalPopulation == 0)
        {
            return 0.0;
        }

        return getNonCityPopulation() * 100.0 / totalPopulation;
    }
}
