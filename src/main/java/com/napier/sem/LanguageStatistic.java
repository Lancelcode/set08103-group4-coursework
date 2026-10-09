package com.napier.sem;

/**
 * One row of the language report: how many people speak a language.
 */
public class LanguageStatistic
{
    /** Language name. */
    public String language;

    /** Estimated number of speakers worldwide. */
    public long speakers;

    /** Speakers as a percentage of the world population. */
    public double worldPercentage;
}