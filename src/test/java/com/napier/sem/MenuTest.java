package com.napier.sem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the text menu (issue #117).
 * The menu is driven with typed input from a Scanner, and a fake App
 * records which report was asked for, so no database is needed.
 */
class MenuTest
{
    /** Message shown when the report number is not on the menu. */
    private static final String BAD_CHOICE = "Please enter a number from 0 to 36";

    /** Message shown when N is not a positive whole number. */
    private static final String BAD_N = "N must be a whole number greater than 0";

    /** Fake app that records the last report the menu ran. */
    private FakeApp app;

    /** Captures everything the menu prints. */
    private ByteArrayOutputStream output;

    /** The real console, restored after each test. */
    private PrintStream original;

    /**
     * Creates a fresh fake app and starts capturing output.
     */
    @BeforeEach
    void setUp()
    {
        app = new FakeApp();
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

    // ---------- Which reports ask for N and a name ----------

    @Test
    void needsNIsTrueOnlyForTopNReports()
    {
        List<Integer> topN = List.of(4, 5, 6, 12, 13, 14, 15, 16, 25, 26, 27);

        for (int choice = 1; choice <= 36; choice++)
        {
            assertEquals(topN.contains(choice), Menu.needsN(choice),
                    "needsN for report " + choice);
        }
    }

    @Test
    void needsNameIsFalseForWorldAndEachReports()
    {
        List<Integer> noName = List.of(1, 4, 7, 12, 17, 18, 19, 20, 21, 22, 25, 28, 29, 30, 31);

        for (int choice = 1; choice <= 36; choice++)
        {
            assertEquals(!noName.contains(choice), Menu.needsName(choice),
                    "needsName for report " + choice);
        }
    }

    @Test
    void nameLabelMatchesTheKindOfPlace()
    {
        assertEquals("continent", Menu.nameLabel(2));
        assertEquals("region", Menu.nameLabel(3));
        assertEquals("country", Menu.nameLabel(10));
        assertEquals("district", Menu.nameLabel(11));
        assertEquals("district", Menu.nameLabel(35));
        assertEquals("city", Menu.nameLabel(36));
    }

    // ---------- Choosing a report and exiting ----------

    @Test
    void zeroExitsWithoutRunningAReport()
    {
        runMenu("0\n");

        assertNull(app.lastCall);
    }

    @Test
    void noInputExitsWithoutError()
    {
        assertDoesNotThrow(() -> runMenu(""));
        assertNull(app.lastCall);
    }

    @Test
    void choiceOutOfRangeShowsMessage()
    {
        runMenu("99\n-1\n0\n");

        assertEquals(2, count(BAD_CHOICE));
        assertNull(app.lastCall);
    }

    @Test
    void textChoiceShowsMessage()
    {
        runMenu("abc\n0\n");

        assertEquals(1, count(BAD_CHOICE));
        assertNull(app.lastCall);
    }

    /**
     * Checks that an unknown report number shows an error message.
     */
    @Test
    void unknownReportShowsMessage()
    {
        Menu menu = new Menu(app, new Scanner(""));

        menu.runReport(99, null, null, 0);

        assertTrue(printed("Unknown report 99"));
    }
    /**
     * Checks that the menu displays a country when results are available.
     */
    @Test
    void countryReportDisplaysResults()
    {
        Country country = new Country();
        country.code = "GBR";
        country.name = "United Kingdom";

        app.countries.add(country);

        runMenu("1\n0\n");

        assertTrue(printed("GBR | United Kingdom"));
    }

    /**
     * Checks that the menu displays a city when results are available.
     */
    @Test
    void cityReportDisplaysResults()
    {
        City city = new City();
        city.name = "Edinburgh";
        city.country = "United Kingdom";
        city.district = "Scotland";
        city.population = 450000;

        app.cities.add(city);

        runMenu("7\n0\n");

        assertEquals("getCities", app.lastCall);
        assertTrue(printed("Edinburgh | United Kingdom | Scotland | 450,000"));
    }
    /**
     * Checks that the menu displays a capital city when results are available.
     */
    @Test
    void capitalReportDisplaysResults()
    {
        CapitalCity capital = new CapitalCity();
        capital.name = "London";
        capital.country = "United Kingdom";
        capital.population = 7000000;

        app.capitals.add(capital);

        runMenu("22\n0\n");

        assertEquals("getCapitalCities", app.lastCall);
        assertTrue(printed("London | United Kingdom | 7,000,000"));
    }

    // ---------- Top N prompt ----------

    @Test
    void topNPassesTheNumberTyped()
    {
        runMenu("4\n5\n0\n");

        assertTrue(printed("Enter N (a positive whole number): "));
        assertEquals("topNCountries 5", app.lastCall);
    }

    @Test
    void topNRejectsZeroNegativeTextAndDecimals()
    {
        runMenu("4\n0\n-3\nabc\n2.5\n3\n0\n");

        assertEquals(4, count(BAD_N));
        assertEquals("topNCountries 3", app.lastCall);
    }

    @Test
    void topNStopsIfInputRunsOutBeforeN()
    {
        assertDoesNotThrow(() -> runMenu("4\n"));
        assertNull(app.lastCall);
    }

    // ---------- Name prompts ----------

    @Test
    void continentReportAsksForContinentAndTrimsIt()
    {
        runMenu("2\n  Europe  \n0\n");

        assertTrue(printed("Enter the continent: "));
        assertEquals("countriesByContinent Europe", app.lastCall);
    }

    @Test
    void topNDistrictAsksForDistrictThenN()
    {
        runMenu("16\nScotland\n3\n0\n");

        assertTrue(printed("Enter the district: "));
        assertTrue(printed("Enter N (a positive whole number): "));
        assertEquals("topNCitiesByDistrict Scotland 3", app.lastCall);
    }

    @Test
    void unknownNameShowsNotFoundMessage()
    {
        runMenu("2\nAtlantis\n0\n");

        assertTrue(printed("No countries found. Check the name is spelled correctly."));
    }

    @Test
    void cityPopulationAsksForCityThenCountry()
    {
        runMenu("36\nEdinburgh\nUnited Kingdom\n0\n");

        assertTrue(printed("Enter the city: "));
        assertTrue(printed("Enter the country: "));
        assertEquals("populationByCity Edinburgh United Kingdom", app.lastCall);
    }

    @Test
    void duplicateCityNamesLetTheUserChoose()
    {
        app.cityTotals.add(total("United States - Richmond (Virginia)", 197790));
        app.cityTotals.add(total("United States - Richmond (California)", 94100));

        runMenu("36\nRichmond\nUnited States\n5\n2\n0\n");

        assertTrue(printed("Multiple cities found:"));
        assertTrue(printed("Please enter a valid number."));
        assertTrue(printed("United States - Richmond (California) | 94,100"));
        assertFalse(printed("United States - Richmond (Virginia) | 197,790"));
    }

    // ---------- Missing input ----------

    /**
     * Checks that the menu exits safely when the country input is missing.
     */
    @Test
    void cityPopulationStopsIfCountryInputRunsOut()
    {
        runMenu("36\nEdinburgh\n");

        assertNull(app.lastCall);
        assertTrue(printed("Enter the country: "));
    }
    /**
     * Checks that the menu exits safely when the continent input is missing.
     */
    @Test
    void nameReportStopsIfInputRunsOut()
    {
        runMenu("2\n");

        assertNull(app.lastCall);
        assertTrue(printed("Enter the continent: "));
    }

    /**
     * Checks that the menu exits safely when no city is selected
     * from multiple matching cities.
     */
    @Test
    void duplicateCitiesStopIfSelectionInputRunsOut()
    {
        app.cityTotals.add(total("United States - Richmond (Virginia)", 197790));
        app.cityTotals.add(total("United States - Richmond (California)", 94100));

        runMenu("36\nRichmond\nUnited States\n");

        assertTrue(printed("Multiple cities found:"));
        assertTrue(printed("Choose a city (1 to 2): "));
        assertFalse(printed("Name | Population"));
    }
    // ---------- Helpers ----------

    /**
     * Runs the menu with the given typed input.
     *
     * @param input lines the user types, separated by \n
     */
    private void runMenu(String input)
    {
        new Menu(app, new Scanner(input)).run();
    }

    /**
     * Checks whether the menu printed some text. Runs of spaces from table
     * padding are collapsed first.
     *
     * @param text text to look for
     * @return true if it was printed
     */
    private boolean printed(String text)
    {
        return output.toString(StandardCharsets.UTF_8).replaceAll(" +", " ").contains(text);
    }

    /**
     * Counts how many times the menu printed some text.
     *
     * @param text text to count
     * @return number of times it was printed
     */
    private int count(String text)
    {
        return output.toString(StandardCharsets.UTF_8).split(text, -1).length - 1;
    }

    /**
     * Makes a population total row.
     *
     * @param name place name
     * @param population population of the place
     * @return the row
     */
    private static PopulationTotal total(String name, long population)
    {
        PopulationTotal total = new PopulationTotal();
        total.name = name;
        total.population = population;
        return total;
    }

    /**
     * App with no database. Records the report the menu asked for
     * and returns rows set by the test (empty by default).
     */
    private static class FakeApp extends App
    {
        /** Last report the menu ran, with its inputs. */
        String lastCall;

        /** Rows returned for a city population lookup. */
        final ArrayList<PopulationTotal> cityTotals = new ArrayList<>();

        final ArrayList<Country> countries = new ArrayList<>();

        final ArrayList<City> cities = new ArrayList<>();

        final ArrayList<CapitalCity> capitals = new ArrayList<>();

        @Override
        public ArrayList<Country> getTopNCountries(int n)
        {
            lastCall = "topNCountries " + n;
            return new ArrayList<>();
        }

        @Override
        public ArrayList<Country> getCountriesByContinent(String continent)
        {
            lastCall = "countriesByContinent " + continent;
            return new ArrayList<>();
        }

        @Override
        public ArrayList<City> getTopNCitiesByDistrict(String district, int n)
        {
            lastCall = "topNCitiesByDistrict " + district + " " + n;
            return new ArrayList<>();
        }

        @Override
        public ArrayList<PopulationTotal> getPopulationByCity(String city, String country)
        {
            lastCall = "populationByCity " + city + " " + country;
            return cityTotals;
        }
        @Override
        public ArrayList<Country> getCountries()
        {
            lastCall = "getCountries";
            return countries;
        }
        @Override
        public ArrayList<City> getCities()
        {
            lastCall = "getCities";
            return cities;
        }
        @Override
        public ArrayList<CapitalCity> getCapitalCities()
        {
            lastCall = "getCapitalCities";
            return capitals;
        }
    }
// ---------- Running all reports ----------

    /**
     * Checks that runAll executes all 36 reports with sample inputs.
     */
    @Test
    void runAllExecutesEveryReport()
    {
        Menu menu = new Menu(app, new Scanner(""));

        assertDoesNotThrow(menu::runAll);

        assertTrue(printed("=== 1. All countries in the world ==="));
        assertTrue(printed("=== 4. Top N countries in the world (N = 5) ==="));
        assertTrue(printed("=== 23. All capital cities in a continent (Europe) ==="));
        assertTrue(printed("=== 36. Population of a city in a country (Edinburgh) ==="));

        assertEquals("populationByCity Edinburgh United Kingdom", app.lastCall);
    }
}
