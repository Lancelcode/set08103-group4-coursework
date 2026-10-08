# USE CASE: 6 View language speaker statistics

## CHARACTERISTIC INFORMATION

### Goal in Context
As an employee I want to see the estimated number of people who speak Chinese, English, Hindi, Spanish and Arabic, so that I can compare how widely these languages are used.

### Scope
Organisation population reporting system.

### Level
Primary task.

### Preconditions
- The MySQL `world` database is running and contains current data.
- The application can connect to the database.

### Success End Condition
The five languages are displayed from most to fewest estimated speakers, each with its number of speakers and its percentage of the world population.

### Failed End Condition
No statistics are displayed and the user is told why.

### Primary Actor
Organisation employee (report user).

### Trigger
The user requests language statistics.

## MAIN SUCCESS SCENARIO
1. The user requests the language statistics report.
2. For each of the five languages, the system multiplies each country's population by that language's recorded percentage in the country.
3. The system adds these estimates across all countries for each language.
4. The system calculates each total as a percentage of the world population.
5. The system sorts the languages from most to fewest speakers.
6. The system displays each language, its estimated speakers and its percentage of the world population.

## EXTENSIONS
2a. A language has no record for a country:
   1. That country adds nothing for the language and the report continues without an error.
2b. The database cannot be reached:
   1. The system shows a clear error message and no report.

## SUB-VARIATIONS
None.

## SCHEDULE
**DUE DATE**: Code Review 3

## LINKS
- User story: US06 (#18)
- Use case diagram: `docs/diagrams/use-case-diagram.png`
