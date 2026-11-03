package com.napier.sem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Connects to the MySQL World database once for every integration test class.
 *
 * <p>Every integration test extends this class and uses the shared {@code app},
 * so no test has to copy connection code. The name does not end in {@code IT},
 * so Failsafe never runs this class on its own.</p>
 *
 * <p>Example:</p>
 * <pre>
 * class CountryReportIT extends IntegrationTestBase
 * {
 *     &#64;Test
 *     void europeHas46Countries()
 *     {
 *         assertEquals(46, app.getCountriesByContinent("Europe").size());
 *     }
 * }
 * </pre>
 *
 * <p>Run with MySQL started: {@code docker compose up -d --wait mysql}, then
 * {@code mvn verify}. Set {@code DB_HOST} if the database is not on localhost.</p>
 */
abstract class IntegrationTestBase
{
    /** Connection attempts before the tests give up. */
    private static final int ATTEMPTS = 3;

    /** Milliseconds to wait between connection attempts. */
    private static final int DELAY_MS = 1000;

    /** App connected to the World database, shared by all tests in the class. */
    protected static App app;

    /**
     * Connects once before any test in the class runs.
     * Fails within a few seconds, with a clear message, if MySQL is not running.
     */
    @BeforeAll
    static void connectToDatabase()
    {
        String host = System.getenv("DB_HOST");
        if (host == null || host.isBlank())
        {
            host = "localhost";
        }

        app = new App();
        app.connect(host, ATTEMPTS, DELAY_MS);
        assertTrue(app.isConnected(),
                "MySQL World database must be running on port 3306");
    }

    /**
     * Closes the connection after every test in the class has run.
     */
    @AfterAll
    static void disconnectFromDatabase()
    {
        if (app != null)
        {
            app.disconnect();
        }
    }
}
