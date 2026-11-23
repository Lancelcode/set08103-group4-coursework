package com.napier.sem;

import java.util.List;
import java.util.Locale;

/**
 * Formats report rows as a table with ASCII borders, for example:
 *
 * <pre>
 * +------+----------------+------------+
 * | Code | Name           | Population |
 * +------+----------------+------------+
 * | GBR  | United Kingdom | 59,623,400 |
 * +------+----------------+------------+
 * </pre>
 *
 * <p>Plain ASCII characters are used so the table looks the same in
 * every terminal, including Windows consoles and Docker logs.</p>
 */
final class TablePrinter
{
    /** Line break used between table lines. */
    private static final String NEW_LINE = System.lineSeparator();

    /**
     * Not used: every method is static.
     */
    private TablePrinter()
    {
        // Utility class, never instantiated
    }

    /**
     * Prints a table to the console.
     *
     * @param headers column titles
     * @param rows one array of cell values per row
     * @param rightAligned true for each column to right-align (numbers)
     */
    static void print(String[] headers, List<String[]> rows, boolean... rightAligned)
    {
        System.out.print(format(headers, rows, rightAligned));
    }

    /**
     * Builds a table as text. Each column is as wide as its longest value.
     *
     * @param headers column titles
     * @param rows one array of cell values per row
     * @param rightAligned true for each column to right-align (numbers)
     * @return the table, one line per border, header and row
     */
    static String format(String[] headers, List<String[]> rows, boolean... rightAligned)
    {
        int[] widths = new int[headers.length];

        for (int i = 0; i < headers.length; i++)
        {
            widths[i] = headers[i].length();
        }

        for (String[] row : rows)
        {
            for (int i = 0; i < widths.length; i++)
            {
                widths[i] = Math.max(widths[i], cell(row, i).length());
            }
        }

        String border = border(widths);
        StringBuilder table = new StringBuilder(border);
        appendRow(table, headers, widths, new boolean[0]);
        table.append(border);

        for (String[] row : rows)
        {
            appendRow(table, row, widths, rightAligned);
        }

        table.append(border);
        return table.toString();
    }

    /**
     * Formats a whole number with thousands separators, for example 59,623,400.
     *
     * @param value the number to format
     * @return the formatted number
     */
    static String number(long value)
    {
        return String.format(Locale.UK, "%,d", value);
    }

    /**
     * Formats a percentage with two decimal places, for example 33.14%.
     *
     * @param value the percentage to format
     * @return the formatted percentage
     */
    static String percent(double value)
    {
        return String.format(Locale.UK, "%.2f%%", value);
    }

    /**
     * Gets a cell value, using an empty string for a missing or null cell.
     *
     * @param row the row
     * @param column the column index
     * @return the cell text
     */
    private static String cell(String[] row, int column)
    {
        if (column >= row.length || row[column] == null)
        {
            return "";
        }

        return row[column];
    }

    /**
     * Builds a border line such as +------+------+.
     *
     * @param widths width of each column
     * @return the border line, ending with a line break
     */
    private static String border(int[] widths)
    {
        StringBuilder line = new StringBuilder();

        for (int width : widths)
        {
            line.append('+').append("-".repeat(width + 2));
        }

        return line.append('+').append(NEW_LINE).toString();
    }

    /**
     * Adds one row of cells, padded to the column widths.
     *
     * @param table the table being built
     * @param row the cell values
     * @param widths width of each column
     * @param rightAligned true for each column to right-align
     */
    private static void appendRow(
            StringBuilder table, String[] row, int[] widths, boolean... rightAligned)
    {
        for (int i = 0; i < widths.length; i++)
        {
            boolean right = i < rightAligned.length && rightAligned[i];
            String pattern = right ? "%" + widths[i] + "s" : "%-" + widths[i] + "s";
            table.append("| ").append(String.format(pattern, cell(row, i))).append(' ');
        }

        table.append('|').append(NEW_LINE);
    }
}
