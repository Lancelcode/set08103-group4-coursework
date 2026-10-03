package com.napier.sem;

/**
 * A row of the country report: one country from the world database.
 */
public class Country
{
    /** Three-letter country code. */
    public String code;
    /** Country name. */
    public String name;
    /** Continent the country is in. */
    public String continent;
    /** Region the country is in. */
    public String region;
    /** Population (long, so world totals also fit). */
    public long population;
    /** Capital city name, or "N/A" if none is recorded. */
    public String capital;
}