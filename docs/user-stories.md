US01 — Country Reports

As a user, I want to view countries by population so that I can compare countries worldwide, in a continent or in a region.

Acceptance Criteria
- Show all countries in the world.
- Show all countries in a selected continent.
- Show all countries in a selected region.
- Show the top N countries in the world.
- Show the top N countries in a selected continent.
- Show the top N countries in a selected region.
- Sort every report by population, largest first.
- Include Code, Name, Continent, Region, Population and Capital.
- Ask the user for N when a Top N report is selected.
- Accept only positive whole numbers for N.
- If N exceeds the available results, show all available results.
- Handle missing capital information without an error.
- Show a clear message for empty or unknown filters.
- Check results against the database.

US02 — City Reports

As a user, I want to view cities by population so that I can compare cities in different locations.
Acceptance Criteria
- Show all cities in the world.
- Show all cities in a selected continent.
- Show all cities in a selected region.
- Show all cities in a selected country.
- Show all cities in a selected district.
- Show the top N cities in the world.
- Show the top N cities in a selected continent.
- Show the top N cities in a selected region.
- Show the top N cities in a selected country.
- Show the top N cities in a selected district.
- Sort every report by population, largest first.
- Include Name, Country, District and Population.
- Ask the user for N when a Top N report is selected.
- Accept only positive whole numbers for N.
- If N exceeds the available results, show all available results.
- Show a clear message for empty or unknown filters.
-  Check results against the database.

 US03 — Capital City Reports

As a user, I want to view capital cities by population so that I can compare capitals worldwide, in a continent or in a region.

 Acceptance Criteria
- Show all capital cities in the world.
- Show all capital cities in a selected continent.
- Show all capital cities in a selected region.
- Show the top N capital cities in the world.
- Show the top N capital cities in a selected continent.
- Show the top N capital cities in a selected region.
- Sort every report by capital city population, largest first.
- Include Name, Country and Population.
- Use the country's capital reference to identify its capital city.
- Handle countries without a recorded capital without an error.
- Accept only positive whole numbers for N.
- If N exceeds the available results, show all available results.
- Show a clear message for empty or unknown filters.
-  Check results against the database.

 US04 — City and Non-City Population Breakdown

As a user, I want to compare city and non-city populations so that I can understand where people live.

 Acceptance Criteria
- Show a population breakdown for each continent.
- Show a population breakdown for each region.
- Show a population breakdown for each country.
- Include the continent, region or country name.
- Show its total population.
- Show the population living in cities and its percentage.
- Show the population not living in cities and its percentage.
- Calculate non-city population as total population minus city population.
- Calculate percentages using the relevant total population.
- Avoid counting a country's population more than once when combining city data.
- Handle zero population without dividing by zero.
- Check totals and percentages against the database.

 US05 — Population Totals

As a user, I want to find the population of a place so that I can access a clear population total.
 Acceptance Criteria
- Show the population of the world.
- Show the population of a selected continent.
- Show the population of a selected region.
- Show the population of a selected country.
- Show the population of a selected district.
- Show the population of a selected city.
- Show the selected location and its population clearly.
- Calculate district population from its recorded cities.
- Ask for enough location information to distinguish matching city or district names.
- Show a clear message when the location is not found.
- Use a numeric type that can store the world population.
- Check results against the database.

US06 — Language Statistics

As a user, I want to see estimated language speaker numbers so that I can compare the use of five languages worldwide.

Acceptance Criteria
- Include Chinese, English, Hindi, Spanish and Arabic.
- Calculate speakers using each country's population and recorded language percentage.
- Add the speaker estimates across countries for each language.
- Show each language's estimated number of speakers.
- Show each language's percentage of the total world population.
- Sort languages by estimated speakers, largest first.
- Handle missing language records without an error.
- Check calculations against the database.

Team Planning

Before implementation, the team will:
- Assign an owner to each story.
- Agree priorities and effort estimates.
- Select the work that fits the CR2 sprint.
- Link each story to its GitHub issue.
- Review completed work against its acceptance criteria.

A grouped story will only be marked complete when all its acceptance criteria are met.
