package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for reports run after the database connection has been
 * closed. Each query fails, so the app must print an error and return an
 * empty result instead of crashing.
 */
class ClosedConnectionIT
{
    /** App whose connection is closed before each test. */
    private App app;

    /** Captures what the app prints. */
    private ByteArrayOutputStream output;

    /** The real console, restored after each test. */
    private PrintStream original;

    /**
     * Connects to the World database, then closes the connection.
     */
    @BeforeEach
    void connectAndClose()
    {
        String host = System.getenv("DB_HOST");
        if (host == null || host.isBlank())
        {
            host = "localhost";
        }

        app = new App();
        app.connect(host, 3, 1000);
        assertTrue(app.isConnected(), "MySQL World database must be running on port 3306");
        app.disconnect();

        output = new ByteArrayOutputStream();
        original = System.out;
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    }

    /**
     * Restores the real console, even if a test failed.
     */
    @AfterEach
    void restoreConsole()
    {
        System.setOut(original);
    }

    /**
     * A report on a closed connection returns an empty list and says why.
     */
    @Test
    void reportReturnsEmptyListWhenQueryFails()
    {
        assertTrue(app.getCountries().isEmpty());
        assertTrue(printed().contains("Failed to get"));
    }

    /**
     * A Top N report on a closed connection also returns an empty list.
     */
    @Test
    void topNReportReturnsEmptyListWhenQueryFails()
    {
        assertTrue(app.getTopNCountries(5).isEmpty());
        assertTrue(printed().contains("Failed to get"));
    }

    /**
     * A population lookup on a closed connection returns 0.
     */
    @Test
    void populationLookupReturnsZeroWhenQueryFails()
    {
        assertEquals(0, app.getWorldPopulation());
        assertTrue(printed().contains("Failed to get world population"));
    }

    /**
     * Gets everything the app printed during the test.
     *
     * @return printed text
     */
    private String printed()
    {
        return output.toString(StandardCharsets.UTF_8);
    }
}
