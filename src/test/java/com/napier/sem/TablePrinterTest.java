package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the table layout used by every report.
 */
class TablePrinterTest
{
    /**
     * A table has a border, the header, a border, one line per row and a border.
     */
    @Test
    void tableHasBordersAroundHeaderAndRows()
    {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] {"GBR", "United Kingdom"});

        List<String> lines = TablePrinter.format(
                new String[] {"Code", "Name"}, rows).lines().toList();

        assertEquals(List.of(
                "+------+----------------+",
                "| Code | Name           |",
                "+------+----------------+",
                "| GBR  | United Kingdom |",
                "+------+----------------+"), lines);
    }

    /**
     * Right-aligned columns pad on the left so numbers line up.
     */
    @Test
    void rightAlignedColumnPadsOnTheLeft()
    {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] {"Edinburgh", "450,180"});
        rows.add(new String[] {"London", "7,285,000"});

        String table = TablePrinter.format(
                new String[] {"Name", "Population"}, rows, false, true);

        assertTrue(table.contains("| Edinburgh |    450,180 |"));
        assertTrue(table.contains("| London    |  7,285,000 |"));
    }

    /**
     * An empty report still shows the header between borders.
     */
    @Test
    void emptyTableShowsOnlyTheHeader()
    {
        String table = TablePrinter.format(new String[] {"Name"}, new ArrayList<>());

        assertEquals(4, table.lines().count());
        assertTrue(table.contains("| Name |"));
    }

    /**
     * Null and missing cells print as blanks instead of crashing.
     */
    @Test
    void nullAndMissingCellsAreBlank()
    {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[] {null});

        String table = TablePrinter.format(new String[] {"Name", "Capital"}, rows);

        assertTrue(table.contains("|      |         |"));
    }

    /**
     * Numbers get thousands separators and percentages two decimal places.
     */
    @Test
    void numbersAndPercentagesAreFormatted()
    {
        assertEquals("1,191,843,539", TablePrinter.number(1_191_843_539L));
        assertEquals("0", TablePrinter.number(0));
        assertEquals("19.61%", TablePrinter.percent(19.6123));
        assertEquals("0.00%", TablePrinter.percent(0));
    }
}
