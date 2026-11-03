package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies population breakdown reports against the MySQL World database.
 * The connected {@code app} comes from {@link IntegrationTestBase}.
 */
class PopulationBreakdownIT extends IntegrationTestBase
{
    /** Independent connection used to calculate expected results. */
    private Connection database;

    /** Country records used to build expected totals. */
    private final ArrayList<CountryRecord> countries = new ArrayList<>();

    /** Recorded city population, indexed by country code. */
    private final Map<String, Long> cityTotals = new HashMap<>();

    /**
     * Reads country and city data through a separate connection,
     * so expected values do not depend on the app's own queries.
     *
     * @throws Exception if the database cannot be accessed
     */
    @BeforeEach
    void setUp() throws Exception
    {
        String host = System.getenv("DB_HOST");
        if (host == null || host.isBlank())
        {
            host = "localhost";
        }

        database = DriverManager.getConnection(
                "jdbc:mysql://" + host
                        + ":3306/world?allowPublicKeyRetrieval=true"
                        + "&useSSL=false&connectTimeout=5000",
                "root", "root");

        try (Statement statement = database.createStatement();
             ResultSet rows = statement.executeQuery(
                     "SELECT CountryCode, Population FROM city"))
        {
            while (rows.next())
            {
                cityTotals.merge(
                        rows.getString("CountryCode"),
                        rows.getLong("Population"),
                        Long::sum);
            }
        }

        try (Statement statement = database.createStatement();
             ResultSet rows = statement.executeQuery(
                     "SELECT Code, Name, Continent, Region, Population "
                             + "FROM country"))
        {
            while (rows.next())
            {
                CountryRecord country = new CountryRecord();
                country.code = rows.getString("Code");
                country.name = rows.getString("Name");
                country.continent = rows.getString("Continent");
                country.region = rows.getString("Region");
                country.population = rows.getLong("Population");
                countries.add(country);
            }
        }

        assertFalse(countries.isEmpty(), "World country data must be loaded");
        assertFalse(cityTotals.isEmpty(), "World city data must be loaded");
    }

    /**
     * Closes the independent connection, including after a failed test.
     * The app's connection is closed by {@link IntegrationTestBase}.
     *
     * @throws Exception if the independent connection cannot close
     */
    @AfterEach
    void tearDown() throws Exception
    {
        if (database != null)
        {
            database.close();
        }
    }

    /**
     * Checks every country's totals against independently read records.
     */
    @Test
    void countryReportMatchesDatabase()
    {
        checkReport(app.getCountryBreakdowns(), expectedTotals("country"));
    }

    /**
     * Checks continent aggregation without multiplying country populations.
     */
    @Test
    void continentReportMatchesDatabase()
    {
        checkReport(app.getContinentBreakdowns(), expectedTotals("continent"));
    }

    /**
     * Checks region aggregation without multiplying country populations.
     */
    @Test
    void regionReportMatchesDatabase()
    {
        checkReport(app.getRegionBreakdowns(), expectedTotals("region"));
    }
