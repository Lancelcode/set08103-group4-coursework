package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for displayPopulationBreakdowns (#121).
 * No database is needed: the rows are built in the test.
 */
class PopulationBreakdownDisplayTest
{
    /** Header printed above every breakdown report. */
    private static final String HEADER = "Name | Total Population | City Population | City % "
            + "| Non-city Population | Non-city %";

    /** App used only for its display method. */
    private final App app = new App();

    /** Captures everything the app prints. */
    private ByteArrayOutputStream output;

    /** The real console, restored after each test. */
    private PrintStream original;

    /**
     * Starts capturing output.
     */
    @BeforeEach
    void setUp()
    {
        output = new ByteArrayOutputStream();
        original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    /**
     * Restores the real console, even if a test failed.
     */
    @AfterEach
    void tearDown()
    {
        System.setOut(original);
    }

    /**
     * A null list prints a message instead of crashing.
     */
    @Test
    void nullListPrintsMessage()
    {
        assertDoesNotThrow(() -> app.displayPopulationBreakdowns(null));
        assertEquals(List.of("No population breakdowns to display"), lines());
    }

    /**
     * An empty list prints only the header.
     */
    @Test
    void emptyListPrintsOnlyHeader()
    {
        app.displayPopulationBreakdowns(new ArrayList<>());

        assertEquals(List.of(HEADER), lines());
    }

    /**
     * A missing row is skipped and the other rows are still printed.
     */
    @Test
    void nullRowIsSkipped()
    {
        ArrayList<PopulationBreakdown> rows = new ArrayList<>();
        rows.add(null);
        rows.add(breakdown("Europe", 730074600L, 241942813L));

        assertDoesNotThrow(() -> app.displayPopulationBreakdowns(rows));
        assertEquals(2, lines().size());
    }

    /**
     * Each row shows the totals and both percentages to 2 decimal places
     * (Europe from US04.1: 33.14% in cities, 66.86% not in cities).
     */
    @Test
    void rowShowsCityAndNonCityPercentages()
    {
        ArrayList<PopulationBreakdown> rows = new ArrayList<>();
        rows.add(breakdown("Europe", 730074600L, 241942813L));

        app.displayPopulationBreakdowns(rows);

        assertEquals(List.of(HEADER,
                "Europe | 730074600 | 241942813 | 33.14% | 488131787 | 66.86%"), lines());
    }

    /**
     * A place with no people shows 0.00% and 0.00%, with no division by zero.
     */
    @Test
    void zeroPopulationShowsZeroPercent()
    {
        ArrayList<PopulationBreakdown> rows = new ArrayList<>();
        rows.add(breakdown("Antarctica", 0L, 0L));

        app.displayPopulationBreakdowns(rows);

        assertEquals(List.of(HEADER, "Antarctica | 0 | 0 | 0.00% | 0 | 0.00%"), lines());
    }

    /**
     * Gets the printed output as separate lines.
     *
     * @return printed lines
     */
    private List<String> lines()
    {
        return output.toString(StandardCharsets.UTF_8).lines().toList();
    }

    /**
     * Makes a breakdown row.
     *
     * @param name continent, region or country
     * @param total total population
     * @param inCities population living in cities
     * @return the row
     */
    private static PopulationBreakdown breakdown(String name, long total, long inCities)
    {
        PopulationBreakdown breakdown = new PopulationBreakdown();
        breakdown.name = name;
        breakdown.totalPopulation = total;
        breakdown.cityPopulation = inCities;
        return breakdown;
    }
}
