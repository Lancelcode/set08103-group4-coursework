package com.napier.sem;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Text menu that lets the user pick a report and enter its inputs.
 */
public class Menu
{
    /** Menu options, in the order they are numbered on screen. */
    private static final String[] OPTIONS = {
            "All countries in the world",
            "All countries in a continent",
            "All countries in a region",
            "Top N countries in the world",
            "Top N countries in a continent",
            "Top N countries in a region",
            "All cities in the world",
            "All cities in a continent",
            "All cities in a region",
            "All cities in a country",
            "All cities in a district",
            "Top N cities in the world",
            "Top N cities in a continent",
            "Top N cities in a region",
            "Top N cities in a country",
            "Top N cities in a district",
            "Population of the world",
            "Population of each continent",
            "Population of each region",
            "Population of each district",
            "Population of each city",
            "All capital cities in the world",
            "All capital cities in a continent",
            "All capital cities in a region",
            "Top N capital cities in the world",
            "Top N capital cities in a continent",
            "Top N capital cities in a region"
    };

    /** The app that runs the database queries. */
    private final App app;

    /** Where the user's input is read from. */
    private final Scanner in;

    /**
     * Creates a menu for the given app.
     *
     * @param app connected app used to run the reports
     * @param in source of user input (usually the keyboard)
     */
    public Menu(App app, Scanner in)
    {
        this.app = app;
        this.in = in;
    }

    /**
     * Shows the menu until the user chooses 0 or input runs out.
     */
    public void run()
    {
        while (true)
        {
            printOptions();

            String line = ask("Choose a report (0 to exit): ");
            if (line == null || line.equals("0"))
            {
                return;
            }

            int choice = parseChoice(line);
            if (choice < 1 || choice > OPTIONS.length)
            {
                System.out.println("Please enter a number from 0 to " + OPTIONS.length);
                continue;
            }

            String name = null;
            if (needsName(choice))
            {
                name = ask("Enter the " + nameLabel(choice) + ": ");
                if (name == null)
                {
                    return;
                }
            }

            int n = 0;
            if (needsN(choice))
            {
                n = askForN();
                if (n == 0)
                {
                    return;
                }
            }

            runReport(choice, name, n);
        }
    }

    /**
     * Runs every report once with sample inputs.
     * Used in Docker and CI, where nobody is there to type a choice.
     */
    public void runAll()
    {
        for (int choice = 1; choice <= OPTIONS.length; choice++)
        {
            String name = needsName(choice) ? sampleName(choice) : null;
            int n = needsN(choice) ? 5 : 0;

            System.out.println();
            System.out.println("=== " + choice + ". " + OPTIONS[choice - 1]
                    + (name != null ? " (" + name + ")" : "")
                    + (n > 0 ? " (N = " + n + ")" : "")
                    + " ===");

            runReport(choice, name, n);
        }
    }

    /**
     * Runs one report and prints its result.
     *
     * @param choice menu number of the report
     * @param name continent, region, country or district name (or null)
     * @param n how many rows for Top N reports (or 0)
     */
    void runReport(int choice, String name, int n)
    {
        switch (choice)
        {
            case 1 -> printCountries(app.getCountries());
            case 2 -> printCountries(app.getCountriesByContinent(name));
            case 3 -> printCountries(app.getCountriesByRegion(name));
            case 4 -> printCountries(app.getTopNCountries(n));
            case 5 -> printCountries(app.getTopNCountriesByContinent(name, n));
            case 6 -> printCountries(app.getTopNCountriesByRegion(name, n));
            case 7 -> printCities(app.getCities());
            case 8 -> printCities(app.getCitiesByContinent(name));
            case 9 -> printCities(app.getCitiesByRegion(name));
            case 10 -> printCities(app.getCitiesByCountry(name));
            case 11 -> printCities(app.getCitiesByDistrict(name));
            case 12 -> printCities(app.getTopNCities(n));
            case 13 -> printCities(app.getTopNCitiesByContinent(name, n));
            case 14 -> printCities(app.getTopNCitiesByRegion(name, n));
            case 15 -> printCities(app.getTopNCitiesByCountry(name, n));
            case 16 -> printCities(app.getTopNCitiesByDistrict(name, n));
            case 17 -> System.out.println("World population: " + app.getWorldPopulation());
            case 18 -> printTotals(app.getContinentPopulations());
            case 19 -> printTotals(app.getRegionPopulations());
            case 20 -> printTotals(app.getDistrictPopulations());
            case 21 -> printTotals(app.getCityPopulations());
            case 22 -> printCapitals(app.getCapitalCities());
            case 23 -> printCapitals(app.getCapitalCitiesByContinent(name));
            case 24 -> printCapitals(app.getCapitalCitiesByRegion(name));
            case 25 -> printCapitals(app.getTopNCapitalCities(n));
            case 26 -> printCapitals(app.getTopNCapitalCitiesByContinent(name, n));
            case 27 -> printCapitals(app.getTopNCapitalCitiesByRegion(name, n));
            default -> System.out.println("Unknown report " + choice);
        }
    }

    /**
     * Prints the numbered list of reports.
     */
    private void printOptions()
    {
        System.out.println();
        System.out.println("POPULATION REPORTS");

        for (int i = 0; i < OPTIONS.length; i++)
        {
            System.out.println((i + 1) + ". " + OPTIONS[i]);
        }

        System.out.println("0. Exit");
    }

    /**
     * Asks a question and returns the trimmed answer.
     *
     * @param prompt text shown to the user
     * @return the answer, or null if there is no more input
     */
    private String ask(String prompt)
    {
        System.out.print(prompt);

        if (!in.hasNextLine())
        {
            return null;
        }

        return in.nextLine().trim();
    }

    /**
     * Keeps asking for N until the user enters a positive whole number.
     *
     * @return N, or 0 if there is no more input
     */
    private int askForN()
    {
        while (true)
        {
            String line = ask("Enter N (a positive whole number): ");
            if (line == null)
            {
                return 0;
            }

            int n = parseChoice(line);
            if (n > 0)
            {
                return n;
            }

            System.out.println("N must be a whole number greater than 0");
        }
    }

    /**
     * Turns text into a number.
     *
     * @param text text typed by the user
     * @return the number, or -1 if the text is not a whole number
     */
    private static int parseChoice(String text)
    {
        try
        {
            return Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            return -1;
        }
    }

    /**
     * Checks whether a report needs a continent, region, country or district.
     *
     * @param choice menu number
     * @return true if the user must enter a name
     */
    static boolean needsName(int choice)
    {
        return (choice >= 2 && choice <= 3)
                || (choice >= 5 && choice <= 6)
                || (choice >= 8 && choice <= 11)
                || (choice >= 13 && choice <= 16)
                || (choice >= 23 && choice <= 24)
                || (choice >= 26 && choice <= 27);
    }

    /**
     * Checks whether a report is a Top N report.
     *
     * @param choice menu number
     * @return true if the user must enter N
     */
    static boolean needsN(int choice)
    {
        return (choice >= 4 && choice <= 6)
                || (choice >= 12 && choice <= 16)
                || (choice >= 25 && choice <= 27);
    }

    /**
     * Says what kind of name a report asks for.
     *
     * @param choice menu number
     * @return "continent", "region", "country" or "district"
     */
    static String nameLabel(int choice)
    {
        return switch (choice)
        {
            case 2, 5, 8, 13, 23, 26 -> "continent";
            case 3, 6, 9, 14, 24, 27 -> "region";
            case 10, 15 -> "country";
            default -> "district";
        };
    }

    /**
     * Gives a sample name for each kind of report, used by runAll().
     *
     * @param choice menu number
     * @return a continent, region, country or district from the World database
     */
    private static String sampleName(int choice)
    {
        return switch (nameLabel(choice))
        {
            case "continent" -> "Europe";
            case "region" -> "Caribbean";
            case "country" -> "United Kingdom";
            default -> "Scotland";
        };
    }

    /**
     * Prints a country report, or a message if nothing matched.
     *
     * @param countries countries to print
     */
    private void printCountries(ArrayList<Country> countries)
    {
        if (countries.isEmpty())
        {
            System.out.println("No countries found. Check the name is spelled correctly.");
            return;
        }

        app.displayCountries(countries);
    }

    /**
     * Prints a city report, or a message if nothing matched.
     *
     * @param cities cities to print
     */
    private void printCities(ArrayList<City> cities)
    {
        if (cities.isEmpty())
        {
            System.out.println("No cities found. Check the name is spelled correctly.");
            return;
        }

        app.displayCities(cities);
    }

    /**
     * Prints a capital city report, or a message if nothing matched.
     *
     * @param capitals capital cities to print
     */
    private void printCapitals(ArrayList<CapitalCity> capitals)
    {
        if (capitals.isEmpty())
        {
            System.out.println("No capital cities found. Check the name is spelled correctly.");
            return;
        }

        app.displayCapitalCities(capitals);
    }

    /**
     * Prints a population totals report.
     *
     * @param totals totals to print
     */
    private void printTotals(ArrayList<PopulationTotal> totals)
    {
        System.out.println("Name | Population");

        for (PopulationTotal total : totals)
        {
            System.out.println(total.name + " | " + total.population);
        }
    }
}