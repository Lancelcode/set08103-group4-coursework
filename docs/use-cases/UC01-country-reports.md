# USE CASE: 1 View country population reports

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to view countries ordered by population, for the world, a continent or a region, so that I can compare countries.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
A country report is displayed, ordered from largest to smallest population, showing Code, Name, Continent, Region, Population and Capital.

### Failed End Condition
No report is displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests a country report.

## MAIN SUCCESS SCENARIO
1. The user chooses a country report: world, continent or region.
2. If a continent or region report is chosen, the user enters the continent or region name.
3. If a Top N report is chosen, the user enters N.
4. The system retrieves the matching countries from the database.
5. The system sorts them from largest to smallest population.
6. The system displays Code, Name, Continent, Region, Population and Capital for each country.

## EXTENSIONS
2a. The continent or region does not exist or has no countries:
   1. The system shows a clear message and no report.
3a. N is not a positive whole number:
   1. The system shows a message and asks for N again.
3b. N is larger than the number of matching countries:
   1. The system shows all matching countries.
6a. A country has no recorded capital:
   1. The system shows N/A for Capital and continues.

## SUB-VARIATIONS
1. All countries in the world, a continent or a region.
2. Top N countries in the world, a continent or a region.

## SCHEDULE
**DUE DATE**: Code Review 2

## LINKS
- User story: US01 (#13), follow-ups #43, #44
- Use case diagram: `docs/diagrams/use-case-diagram.png`
