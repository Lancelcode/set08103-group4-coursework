# USE CASE: 4 View city and non-city population breakdown

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to see, for each continent, region or country, how many people live in cities and how many do not, so that I can understand where people live.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
A breakdown is displayed for every continent, region or country showing the name, total population, population in cities (with %) and population not in cities (with %).

### Failed End Condition
No breakdown is displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests a population breakdown.

## MAIN SUCCESS SCENARIO
1. The user chooses a breakdown level: continent, region or country.
2. The system calculates the total population of each area from the country table.
3. The system calculates the population living in cities from the city table, counting each country's population only once.
4. The system calculates the non-city population as total minus city population.
5. The system calculates both percentages using the area's total population.
6. The system displays the name, total, city population and %, and non-city population and % for each area.

## EXTENSIONS
5a. An area has a total population of zero:
   1. The system shows 0% for both percentages instead of dividing by zero.
2a. The database cannot be reached:
   1. The system shows a clear error message and no breakdown.

## SUB-VARIATIONS
1. Breakdown per continent.
2. Breakdown per region.
3. Breakdown per country.

## SCHEDULE
**DUE DATE**: Code Review 3

## LINKS
- User story: US04 (#16)
- Use case diagram: `docs/diagrams/use-case-diagram.png`
