# Issue #16 – Population Breakdown Testing

Implemented city and non-city population reports for continents,
regions and countries.

## Checks completed
- Continent and region city populations matched independent database queries.
- Application ran successfully with exit code 0.
- Continent totals matched direct database queries.
- Pakistan's total population and city population matched the database.
- Pakistan: total 156,483,000; city 31,546,745;
  non-city 124,936,255; city 20.16%; non-city 79.84%.
- Antarctica displayed zero totals and 0.00% without division errors.
- Queries sum city populations per country before joining,
  preventing country totals from being counted multiple times.

## Known data inconsistency
Cocos (Keeling) Islands has a country population of 600,
but its two city populations total 670 (503 + 167).
The report therefore shows -70 non-city population and 111.67% city
population. These values follow the required formula and the database
data; they have not been adjusted.

## Remaining checks
- Run CI checks and obtain teammate review.