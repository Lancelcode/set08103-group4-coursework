package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the language report display.
 */
class LanguageReportTest
{
    /**
     * A null list does not throw.
     */
    @Test
    void nullLanguageStatisticsDoesNotThrow()
    {
        App app = new App();

        assertDoesNotThrow(() -> app.displayLanguageStatistics(null));
    }

    /**
     * A null list prints a message.
     */
    @Test
    void nullLanguageStatisticsDisplaysMessage()
    {
        App app = new App();

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;

        try
        {
            System.setOut(new java.io.PrintStream(output));

            app.displayLanguageStatistics(null);

            assertTrue(output.toString().contains("No language statistics to display"));
        }
        finally
        {
            System.setOut(originalOut);
        }
    }

    /**
     * An empty list prints the table header only.
     */
    @Test
    void emptyLanguageStatisticsDisplaysHeader()
    {
        App app = new App();

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;

        try
        {
            System.setOut(new java.io.PrintStream(output));

            app.displayLanguageStatistics(new ArrayList<>());

            String result = output.toString();

            assertTrue(squash(result).contains("| Language | Speakers | % of world population |"));
            assertEquals(4, result.lines().count());
        }
        finally
        {
            System.setOut(originalOut);
        }
    }

    /**
     * A null row is skipped.
     */
    @Test
    void nullLanguageEntryIsSkipped()
    {
        App app = new App();

        ArrayList<LanguageStatistic> stats = new ArrayList<>();
        stats.add(null);

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;

        try
        {
            System.setOut(new java.io.PrintStream(output));

            assertDoesNotThrow(() -> app.displayLanguageStatistics(stats));

            String result = output.toString();

            assertTrue(squash(result).contains("| Language | Speakers | % of world population |"));
            assertEquals(4, result.lines().count());
        }
        finally
        {
            System.setOut(originalOut);
        }
    }

    /**
     * A language row shows the speakers and the percentage.
     */
    @Test
    void chineseLanguageStatisticsDisplaysCorrectly()
    {
        App app = new App();

        LanguageStatistic chinese = new LanguageStatistic();
        chinese.language = "Chinese";
        chinese.speakers = 1191843539L;
        chinese.worldPercentage = 19.61;

        ArrayList<LanguageStatistic> stats = new ArrayList<>();
        stats.add(chinese);

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;

        try
        {
            System.setOut(new java.io.PrintStream(output));

            app.displayLanguageStatistics(stats);

            String result = output.toString();

            assertTrue(squash(result).contains("| Chinese | 1,191,843,539 | 19.61% |"));
        }
        finally
        {
            System.setOut(originalOut);
        }
    }

    /**
     * Four languages print four rows.
     */
    @Test
    void fourLanguagesDisplayFourRows()
    {
        App app = new App();

        ArrayList<LanguageStatistic> stats = new ArrayList<>();

        String[] languages = {"Chinese", "Hindi", "Spanish", "English"};

        for (String language : languages)
        {
            LanguageStatistic stat = new LanguageStatistic();
            stat.language = language;
            stat.speakers = 1000000L;
            stat.worldPercentage = 1.5;
            stats.add(stat);
        }

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;

        try
        {
            System.setOut(new java.io.PrintStream(output));

            app.displayLanguageStatistics(stats);

            String result = output.toString();

            assertEquals(8, result.lines().count());

            for (String language : languages)
            {
                assertTrue(squash(result).contains("| " + language + " | "));
            }
        }
        finally
        {
            System.setOut(originalOut);
        }
    }

    /**
     * Collapses runs of spaces, so table padding does not affect the checks.
     *
     * @param text printed output
     * @return the output with single spaces
     */
    private static String squash(String text)
    {
        return text.replaceAll(" +", " ");
    }
}
