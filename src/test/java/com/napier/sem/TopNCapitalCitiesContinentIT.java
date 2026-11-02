package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for US03.5: Top N capital cities in a continent (#150).
 * Checks getTopNCapitalCitiesByContinent against the MySQL World database.
 * Run with MySQL started: docker compose up -d --wait mysql, then mvn verify.
 */
class TopNCapitalCitiesContinentIT extends IntegrationTestBase
{
    /**
     * Europe, N = 2 gives Moscow then London.
     */
    @Test
    void topTwoInEurope()
    {
        assertEquals(List.of("Moscow", "London"),
                names(app.getTopNCapitalCitiesByContinent("Europe", 2)));
    }

    /**
     * A continent that does not exist gives an empty list, not an error.
     */
    @Test
    void unknownContinentReturnsNoCapitals()
    {
        assertTrue(app.getTopNCapitalCitiesByContinent("Atlantis", 3).isEmpty());
    }

    /**
     * N must be greater than 0.
     */
    @Test
    void zeroNGivesNoCapitals()
    {
        assertTrue(app.getTopNCapitalCitiesByContinent("Europe", 0).isEmpty());
    }

    /**
     * Gets the names of the given capital cities, in order.
     *
     * @param capitals capital cities from a report
     * @return their names
     */
    private static List<String> names(ArrayList<CapitalCity> capitals)
    {
        return capitals.stream().map(capital -> capital.name).toList();
    }
}
