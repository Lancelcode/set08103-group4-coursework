# USE CASE: 3 View capital city population reports

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to view capital cities ordered by population, for the world, a continent or a region, so that I can compare capitals.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
A capital city report is displayed, ordered from largest to smallest capital population, showing Name, Country and Population.

### Failed End Condition
No report is displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests a capital city report.

## MAIN SUCCESS SCENARIO
1. The user chooses a capital city report: world, continent or region.
2. If a continent or region report is chosen, the user enters its name.
3. If a Top N report is chosen, the user enters N.
4. The system finds each country's capital using the country's capital reference.
5. The system sorts the capitals from largest to smallest population.
6. The system displays Name, Country and Population for each capital.

## EXTENSIONS
2a. The continent or region does not exist or has no capitals:
   1. The system shows a clear message and no report.
3a. N is not a positive whole number:
   1. The system shows a message and asks for N again.
3b. N is larger than the number of matching capitals:
   1. The system shows all matching capitals.
4a. A country has no recorded capital:
   1. The country is left out of the report without an error.

## SUB-VARIATIONS
1. All capital cities in the world, a continent or a region.
2. Top N capital cities in the world, a continent or a region.

## SCHEDULE
**DUE DATE**: Code Review 2

## LINKS
- User story: US03 (#15), PR #42
- Use case diagram: `docs/diagrams/use-case-diagram.png`
