# USE CASE: 5 Look up the population of a place

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to find the population of the world, a continent, a region, a country, a district or a city, so that I can access a clear population total.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
The selected place and its population are displayed.

### Failed End Condition
No population is displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests the population of a place.

## MAIN SUCCESS SCENARIO
1. The user chooses the type of place: world, continent, region, country, district or city.
2. Unless the world is chosen, the user enters the place name.
3. For a district or city, the user also enters the country so duplicate names can be told apart.
4. The system calculates the population (district population is the sum of its recorded cities).
5. The system displays the place and its population.

## EXTENSIONS
2a. The place is not found:
   1. The system shows a clear message and no population.
3a. The name still matches more than one place:
   1. The system lists the matches and asks the user to choose one.

## SUB-VARIATIONS
1. Population of the world, a continent, a region, a country, a district or a city.

## SCHEDULE
**DUE DATE**: Code Review 3

## LINKS
- User story: US05 (#17), follow-up #47
- Use case diagram: `docs/diagrams/use-case-diagram.png`
