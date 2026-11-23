package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for reading settings such as DB_USER and DB_PASSWORD.
 * No database is needed.
 */
class AppSettingsTest
{
    /**
     * A missing environment variable falls back to the default.
     */
    @Test
    void missingValueUsesDefault()
    {
        assertEquals("root", App.valueOrDefault(null, "root"));
    }

    /**
     * An empty or blank environment variable falls back to the default.
     */
    @Test
    void blankValueUsesDefault()
    {
        assertEquals("root", App.valueOrDefault("", "root"));
        assertEquals("root", App.valueOrDefault("   ", "root"));
    }

    /**
     * A value that is set is used as it is.
     */
    @Test
    void setValueIsUsed()
    {
        assertEquals("report_user", App.valueOrDefault("report_user", "root"));
    }
}
