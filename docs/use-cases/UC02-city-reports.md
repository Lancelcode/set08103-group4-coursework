# USE CASE: 2 View city population reports

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to view cities ordered by population, for the world, a continent, a region, a country or a district, so that I can compare cities in different locations.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
A city report is displayed, ordered from largest to smallest population, showing Name, Country, District and Population.

### Failed End Condition
No report is displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests a city report.

## MAIN SUCCESS SCENARIO
1. The user chooses a city report: world, continent, region, country or district.
2. Unless the world is chosen, the user enters the name of the area.
3. If a Top N report is chosen, the user enters N.
4. The system retrieves the matching cities from the database.
5. The system sorts them from largest to smallest population.
6. The system displays Name, Country, District and Population for each city.

## EXTENSIONS
2a. The area does not exist or has no cities:
   1. The system shows a clear message and no report.
3a. N is not a positive whole number:
   1. The system shows a message and asks for N again.
3b. N is larger than the number of matching cities:
   1. The system shows all matching cities.

## SUB-VARIATIONS
1. All cities in the world, a continent, a region, a country or a district.
2. Top N cities in the world, a continent, a region, a country or a district.

## SCHEDULE
**DUE DATE**: Code Review 2

## LINKS
- User story: US02 (#14)
- Use case diagram: `docs/diagrams/use-case-diagram.png`
