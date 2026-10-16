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
 */
class PopulationBreakdownIT
{
    /** Application being tested. */
    private App app;

    /** Independent connection used to calculate expected results. */
    private Connection database;

    /** Country records used to build expected totals. */
    private final ArrayList<CountryRecord> countries = new ArrayList<>();

    /** Recorded city population, indexed by country code. */
    private final Map<String, Long> cityTotals = new HashMap<>();

    /**
     * Connects to MySQL and reads country and city data separately.
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

        app = new App();
        app.connect(host, 3, 1000);
        assertTrue(app.isConnected(),
                "MySQL World database must be running on port 3306");

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
     * Closes both connections, including after a failed test.
     *
     * @throws Exception if the independent connection cannot close
     */
    @AfterEach
    void tearDown() throws Exception
    {
        try
        {
            if (app != null)
            {
                app.disconnect();
            }
        }
        finally
        {
            if (database != null)
            {
                database.close();
            }
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

    /**
     * Checks that countries without recorded cities remain in the report.
     */
    @Test
    void includesCountriesWithoutCities()
    {
        Map<String, PopulationBreakdown> report =
                indexReport(app.getCountryBreakdowns());
        int checked = 0;

        for (CountryRecord country : countries)
        {
            if (!cityTotals.containsKey(country.code))
            {
                PopulationBreakdown row = report.get(country.name);
                assertNotNull(row, "Missing country: " + country.name);
                assertEquals(0L, row.cityPopulation, country.name);
                assertEquals(country.population,
                        row.getNonCityPopulation(), country.name);
                checked++;
            }
        }

        assertTrue(checked > 0,
                "Dataset must contain countries without recorded cities");
    }

    /**
     * Checks percentages for countries whose total population is zero.
     */
    @Test
    void handlesZeroPopulationCountries()
    {
        Map<String, PopulationBreakdown> report =
                indexReport(app.getCountryBreakdowns());
        int checked = 0;

        for (CountryRecord country : countries)
        {
            if (country.population == 0L)
            {
                PopulationBreakdown row = report.get(country.name);
                assertNotNull(row, "Missing country: " + country.name);
                assertEquals(0L, row.totalPopulation, country.name);
                assertEquals(0.0, row.getCityPercentage(), 0.000001);
                assertEquals(0.0, row.getNonCityPercentage(), 0.000001);
                checked++;
            }
        }

        assertTrue(checked > 0,
                "Dataset must contain zero-population countries");
    }

    /**
     * Checks descending population order for all three reports.
     */
    @Test
    void reportsAreOrderedByPopulation()
    {
        checkOrder(app.getCountryBreakdowns());
        checkOrder(app.getContinentBreakdowns());
        checkOrder(app.getRegionBreakdowns());
    }

    /**
     * Calculates expected totals in Java from separate table reads.
     *
     * @param scope country, continent or region
     * @return total and city populations indexed by area name
     */
    private Map<String, long[]> expectedTotals(String scope)
    {
        Map<String, long[]> expected = new HashMap<>();

        for (CountryRecord country : countries)
        {
            String name = switch (scope)
            {
                case "country" -> country.name;
                case "continent" -> country.continent;
                case "region" -> country.region;
                default -> throw new IllegalArgumentException(scope);
            };

            long[] totals = expected.computeIfAbsent(name, key -> new long[2]);
            totals[0] += country.population;
            totals[1] += cityTotals.getOrDefault(country.code, 0L);
        }

        return expected;
    }

    /**
     * Checks names, row counts and population values.
     *
     * @param rows application report
     * @param expected independently calculated population totals
     */
    private void checkReport(
            ArrayList<PopulationBreakdown> rows,
            Map<String, long[]> expected)
    {
        Map<String, PopulationBreakdown> actual = indexReport(rows);
        assertEquals(expected.keySet(), actual.keySet());

        for (Map.Entry<String, long[]> entry : expected.entrySet())
        {
            String name = entry.getKey();
            long[] totals = entry.getValue();
            PopulationBreakdown row = actual.get(name);

            assertEquals(totals[0], row.totalPopulation, name);
            assertEquals(totals[1], row.cityPopulation, name);
            assertEquals(totals[0] - totals[1],
                    row.getNonCityPopulation(), name);
        }
    }

    /**
     * Indexes report rows and rejects duplicate area names.
     *
     * @param rows application report
     * @return report rows indexed by name
     */
    private Map<String, PopulationBreakdown> indexReport(
            ArrayList<PopulationBreakdown> rows)
    {
        assertNotNull(rows);
        assertFalse(rows.isEmpty(), "Report must contain rows");

        Map<String, PopulationBreakdown> indexed = new HashMap<>();
        for (PopulationBreakdown row : rows)
        {
            assertNotNull(row);
            assertNull(indexed.put(row.name, row),
                    "Duplicate report name: " + row.name);
        }
        return indexed;
    }

    /**
     * Checks that report populations never increase between rows.
     *
     * @param rows report ordered by descending population
     */
    private void checkOrder(ArrayList<PopulationBreakdown> rows)
    {
        assertNotNull(rows);
        assertFalse(rows.isEmpty());

        for (int i = 1; i < rows.size(); i++)
        {
            assertTrue(
                    rows.get(i - 1).totalPopulation
                            >= rows.get(i).totalPopulation,
                    "Report must be ordered by descending population");
        }
    }

    /**
     * Stores one independent country record for expected calculations.
     */
    private static class CountryRecord
    {
        /** Country code used to match recorded cities. */
        private String code;

        /** Country name. */
        private String name;

        /** Continent name. */
        private String continent;

        /** Region name. */
        private String region;

        /** Country population, counted once during aggregation. */
        private long population;
    }
}