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
 * Unit tests for how App behaves when it has no database connection.
 * No MySQL is needed: every report method must return an empty list
 * (or zero) and print a clear message, instead of crashing.
 */
class AppWithoutDatabaseTest
{
    /** Message every report prints when there is no connection. */
    private static final String NO_CONNECTION = "No database connection";

    /** App that has never been connected. */
    private App app;

    /** Captures everything the app prints. */
    private ByteArrayOutputStream output;

    /** The real console, restored after each test. */
    private PrintStream original;

    /**
     * Creates a disconnected app and starts capturing output.
     */
    @BeforeEach
    void setUp()
    {
        app = new App();
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
     * Counts how many times the no-connection message was printed.
     *
     * @return number of times the message appears in the output
     */
    private int noConnectionMessages()
    {
        String printed = output.toString(StandardCharsets.UTF_8);
        return printed.split(NO_CONNECTION, -1).length - 1;
    }

    /**
     * Checks every list is empty (not null) and each printed the message once.
     *
     * @param results the lists returned by the report methods
     */
    private void assertAllEmptyWithMessage(List<? extends List<?>> results)
    {
        for (List<?> result : results)
        {
            assertNotNull(result, "A report returned null instead of an empty list");
            assertTrue(result.isEmpty(), "A report returned rows without a database");
        }
        assertEquals(results.size(), noConnectionMessages(),
                "Each report should print '" + NO_CONNECTION + "' once");
    }

    // ---- connection ----

    /**
     * A new app starts disconnected.
     */
    @Test
    void newAppIsNotConnected()
    {
        assertFalse(app.isConnected());
    }

    /**
     * Connecting to a host that does not exist leaves the app disconnected
     * and says so, after the given number of attempts.
     */
    @Test
    void connectToUnknownHostLeavesAppDisconnected()
    {
        app.connect("no-such-host.invalid", 1, 0);

        assertFalse(app.isConnected());
        assertTrue(output.toString(StandardCharsets.UTF_8)
                .contains("Could not connect to database after 1 attempts"));
    }

    /**
     * Disconnecting an app that was never connected does not throw.
     */
    @Test
    void disconnectWithoutConnectionDoesNotThrow()
    {
        assertDoesNotThrow(() -> app.disconnect());
    }

    // ---- country reports ----

    /**
     * All six country reports (US01) return an empty list without a database.
     */
    @Test
    void countryReportsReturnEmptyListsWithoutDatabase()
    {
        List<ArrayList<Country>> results = List.of(
                app.getCountries(),
                app.getCountriesByContinent("Europe"),
                app.getCountriesByRegion("Caribbean"),
                app.getTopNCountries(5),
                app.getTopNCountriesByContinent("Europe", 3),
                app.getTopNCountriesByRegion("Caribbean", 3));

        assertAllEmptyWithMessage(results);
    }

    // ---- city reports ----

    /**
     * All ten city reports (US02) return an empty list without a database.
     */
    @Test
    void cityReportsReturnEmptyListsWithoutDatabase()
    {
        List<ArrayList<City>> results = List.of(
                app.getCities(),
                app.getCitiesByContinent("Europe"),
                app.getCitiesByRegion("Caribbean"),
                app.getCitiesByCountry("United Kingdom"),
                app.getCitiesByDistrict("Scotland"),
                app.getTopNCities(3),
                app.getTopNCitiesByContinent("Europe", 2),
                app.getTopNCitiesByRegion("Caribbean", 3),
                app.getTopNCitiesByCountry("United Kingdom", 3),
                app.getTopNCitiesByDistrict("Scotland", 2));

        assertAllEmptyWithMessage(results);
    }
}
