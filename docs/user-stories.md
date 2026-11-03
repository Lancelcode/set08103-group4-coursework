# User stories - SET08103 Group 4 (population reporting system)

Every required report and lookup in the brief has its own user story and GitHub issue. Each story names a real role, says what the user gets and why, lists Given / When / Then acceptance criteria with the correct answer from the World database, and is proved by named automated tests (listed in its issue).

## What changed after CR2

At Code Review 2, Andreas gave us two pieces of feedback. This is what we changed:

| Feedback | Problem | What we did |
|---|---|---|
| User stories were too vague | Each story said "As a user" (Unit 05b: *user* is not a legitimate role), grouped up to ten reports, and had criteria such as "show a clear message" that could not be tested | The six grouped stories (#13 - #18) are now **epics**. They were split into **32 specific stories** (#130 - #161), one per report or lookup in the brief, each with a specific role, measurable criteria with real expected values, and the tests that prove it (#125) |
| The database was missing from the use case diagram | The use case diagram only showed the report user | The MySQL World database is shown as a **secondary actor** on the right of the system boundary, and named in UC01 - UC06 (#124) |

## User roles

| Role | Who they are |
|---|---|
| Population analyst (research team) | Produces population summaries and rankings of countries for the organisation |
| Urban planning analyst | Compares cities to plan infrastructure and urban programmes |
| Policy researcher (government relations team) | Prepares briefings about capital cities and decides which to prioritise |
| Regional development analyst | Compares how urbanised each area is to plan rural and urban programmes |
| Statistics officer | Answers one-off population enquiries about a single place |
| Language services manager | Decides which languages translated reports are produced in first |

## Old stories (epics) and the specific stories they were split into

| Epic | Area | Specific stories | Role | Owner |
|---|---|---|---|---|
| #13 US01 | Country reports | US01.1 - US01.6 (#130 - #135) | population analyst | Julia |
| #14 US02 | City reports | US02.1 - US02.10 (#136 - #145) | urban planning analyst | Hassan |
| #15 US03 | Capital city reports | US03.1 - US03.6 (#146 - #151) | policy researcher | Julia |
| #16 US04 | City and non-city population breakdown | US04.1 - US04.3 (#152 - #154) | regional development analyst | Zain |
| #17 US05 | Population totals | US05.1 - US05.6 (#155 - #160) | statistics officer | Kyle |
| #18 US06 | Language statistics | US06.1 - US06.1 (#161 - #161) | language services manager | Kyle |

## The stories

Full criteria, expected values and test names are in each GitHub issue. The criteria below are the ones each story is accepted against.

### US01 - Country reports (epic #13)

#### US01.1: All countries in the world, largest population first (#130)
*Menu option 1, brief report 1, owner Julia*

As a population analyst in the research team, I want to see every country in the world ordered from largest to smallest population, so that I can rank countries in the organisation's global population summary.

- [ ] Given the World database is loaded, when I choose option 1, then 239 countries are listed.
- [ ] Each row shows Code, Name, Continent, Region, Population and Capital, in that order.
- [ ] The first three rows are China (CHN, 1,277,558,000, capital Peking), India (IND, 1,013,662,000, New Delhi) and United States (USA, 278,357,000, Washington).
- [ ] Every row has a population greater than or equal to the row below it.
- [ ] The 7 countries with no recorded capital (for example Antarctica and Bouvet Island) are listed with Capital `N/A`, and no error is printed.

#### US01.2: All countries in a continent, largest population first (#131)
*Menu option 2, brief report 2, owner Julia*

As a population analyst in the research team, I want to see every country in a continent I choose, ordered by population, so that I can compare the countries of one continent in a continental briefing.

- [ ] When I choose option 2 and enter `Europe`, then 46 countries are listed and every row has Continent `Europe`.
- [ ] The first three rows are Russian Federation (146,934,000), Germany (82,164,700) and United Kingdom (59,623,400), and the list is ordered largest population first.
- [ ] The columns are the same as US01.1: Code, Name, Continent, Region, Population, Capital.
- [ ] When I enter a continent that does not exist, such as `Atlantis`, then `No countries found. Check the name is spelled correctly.` is printed and the menu is shown again, with no exception.

#### US01.3: All countries in a region, largest population first (#132)
*Menu option 3, brief report 3, owner Julia*

As a population analyst in the research team, I want to see every country in a region I choose, ordered by population, so that I can compare neighbouring countries in a regional report.

- [ ] When I choose option 3 and enter `Caribbean`, then 24 countries are listed and every row has Region `Caribbean`.
- [ ] The first three rows are Cuba (11,201,000), Dominican Republic (8,495,000) and Haiti (8,222,000), and the list is ordered largest population first.
- [ ] When I enter a region that does not exist, such as `Middle Earth`, then `No countries found. Check the name is spelled correctly.` is printed, with no exception.

#### US01.4: Top N countries in the world (#133)
*Menu option 4, brief report 4, owner Julia*

As a population analyst in the research team, I want to see only the N most populated countries in the world, with N chosen by me, so that I can produce a short top-N table without reading all 239 countries.

- [ ] When I choose option 4 and enter N = 5, then exactly 5 rows are shown: China, India, United States, Indonesia, Brazil, in that order.
- [ ] When I enter N = 300 (more than the 239 countries), then all 239 countries are shown and no error is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.
- [ ] `getTopNCountries(0)` returns an empty list and prints `N must be greater than 0`.

#### US01.5: Top N countries in a continent (#134)
*Menu option 5, brief report 5, owner Julia*

As a population analyst in the research team, I want to see the N most populated countries in a continent I choose, so that I can highlight the biggest countries of one continent in a summary.

- [ ] When I choose option 5, enter `Europe` and N = 3, then exactly Russian Federation, Germany and United Kingdom are shown, in that order.
- [ ] When I enter `Europe` and N = 100, then all 46 European countries are shown and no error is printed.
- [ ] When I enter `Atlantis` and N = 3, then `No countries found. Check the name is spelled correctly.` is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US01.6: Top N countries in a region (#135)
*Menu option 6, brief report 6, owner Julia*

As a population analyst in the research team, I want to see the N most populated countries in a region I choose, so that I can highlight the biggest countries of one region in a regional summary.

- [ ] When I choose option 6, enter `Caribbean` and N = 3, then exactly Cuba, Dominican Republic and Haiti are shown, in that order.
- [ ] When I enter `British Islands` and N = 10, then only 2 rows are shown, United Kingdom then Ireland, with no error.
- [ ] When I enter `Middle Earth` and N = 3, then `No countries found. Check the name is spelled correctly.` is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

### US02 - City reports (epic #14)

#### US02.1: All cities in the world, largest population first (#136)
*Menu option 7, brief report 7, owner Hassan*

As an urban planning analyst, I want to see every city in the world ordered from largest to smallest population, so that I can identify the largest urban areas for infrastructure comparisons.

- [ ] Given the World database is loaded, when I choose option 7, then 4,079 cities are listed.
- [ ] Each row shows Name, Country, District and Population, in that order. Country is the country name (for example `India`), not the code (`IND`).
- [ ] The first three rows are Mumbai (Bombay), India, Maharashtra, 10,500,000; Seoul, South Korea, Seoul, 9,981,619; and São Paulo, Brazil, São Paulo, 9,968,485.
- [ ] Every row has a population greater than or equal to the row below it, and accented names such as `São Paulo` print correctly.

#### US02.2: All cities in a continent, largest population first (#137)
*Menu option 8, brief report 8, owner Hassan*

As an urban planning analyst, I want to see every city in a continent I choose, ordered by population, so that I can compare cities across one continent.

- [ ] When I choose option 8 and enter `Europe`, then 841 cities are listed.
- [ ] The first two rows are Moscow (Russian Federation, Moscow (City), 8,389,200) and London (United Kingdom, England, 7,285,000), and the list is ordered largest population first.
- [ ] When I enter `Atlantis`, then `No cities found. Check the name is spelled correctly.` is printed, with no exception.

#### US02.3: All cities in a region, largest population first (#138)
*Menu option 9, brief report 9, owner Hassan*

As an urban planning analyst, I want to see every city in a region I choose, ordered by population, so that I can compare cities across neighbouring countries.

- [ ] When I choose option 9 and enter `Caribbean`, then 58 cities are listed.
- [ ] The first three rows are La Habana (Cuba, 2,256,000), Santo Domingo de Guzmán (Dominican Republic, 1,609,966) and Port-au-Prince (Haiti, 884,472).
- [ ] When I enter `Middle Earth`, then `No cities found. Check the name is spelled correctly.` is printed.

#### US02.4: All cities in a country, largest population first (#139)
*Menu option 10, brief report 10, owner Hassan*

As an urban planning analyst, I want to see every city in a country I choose, ordered by population, so that I can see how the country's urban population is spread across its cities.

- [ ] When I choose option 10 and enter `United Kingdom`, then 81 cities are listed and every row has Country `United Kingdom`.
- [ ] The first three rows are London (7,285,000), Birmingham (1,013,000) and Glasgow (619,680).
- [ ] When I enter `Narnia`, then `No cities found. Check the name is spelled correctly.` is printed.

#### US02.5: All cities in a district, largest population first (#140)
*Menu option 11, brief report 11, owner Hassan*

As an urban planning analyst, I want to see every city in a district I choose, ordered by population, so that I can compare the cities of one district for local planning.

- [ ] When I choose option 11 and enter `Scotland`, then exactly 4 cities are listed: Glasgow (619,680), Edinburgh (450,180), Aberdeen (213,070), Dundee (146,690).
- [ ] When I enter `Distrito Federal`, a district name used in Argentina, Brazil, Mexico and Venezuela, then 5 cities are listed, with Ciudad de México (Mexico, 8,591,309) first. The Country column lets the reader tell them apart.
- [ ] When I enter `Atlantis`, then `No cities found. Check the name is spelled correctly.` is printed.

#### US02.6: Top N cities in the world (#141)
*Menu option 12, brief report 12, owner Hassan*

As an urban planning analyst, I want to see only the N largest cities in the world, with N chosen by me, so that I can list the biggest cities without reading all 4,079.

- [ ] When I choose option 12 and enter N = 3, then exactly Mumbai (Bombay), Seoul and São Paulo are shown, in that order.
- [ ] When I enter N = 5000, then all 4,079 cities are shown and no error is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US02.7: Top N cities in a continent (#142)
*Menu option 13, brief report 13, owner Hassan*

As an urban planning analyst, I want to see the N largest cities in a continent I choose, so that I can compare the leading cities of one continent.

- [ ] When I choose option 13, enter `Europe` and N = 2, then exactly Moscow then London are shown.
- [ ] When I enter `Atlantis` and N = 3, then `No cities found. Check the name is spelled correctly.` is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US02.8: Top N cities in a region (#143)
*Menu option 14, brief report 14, owner Hassan*

As an urban planning analyst, I want to see the N largest cities in a region I choose, so that I can compare the leading cities of one region.

- [ ] When I choose option 14, enter `Caribbean` and N = 3, then exactly La Habana, Santo Domingo de Guzmán and Port-au-Prince are shown, in that order.
- [ ] When I enter `Middle Earth` and N = 3, then `No cities found. Check the name is spelled correctly.` is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US02.9: Top N cities in a country (#144)
*Menu option 15, brief report 15, owner Hassan*

As an urban planning analyst, I want to see the N largest cities in a country I choose, so that I can focus a national report on its main cities.

- [ ] When I choose option 15, enter `United Kingdom` and N = 3, then exactly London, Birmingham and Glasgow are shown, in that order.
- [ ] When I enter `United Kingdom` and N = 100, then all 81 cities are shown.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US02.10: Top N cities in a district (#145)
*Menu option 16, brief report 16, owner Hassan*

As an urban planning analyst, I want to see the N largest cities in a district I choose, so that I can focus local planning on its main cities.

- [ ] When I choose option 16, enter `Scotland` and N = 2, then exactly Glasgow then Edinburgh are shown.
- [ ] When I enter `Scotland` and N = 10, then only the 4 Scottish cities are shown, with no error.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

### US03 - Capital city reports (epic #15)

#### US03.1: All capital cities in the world, largest population first (#146)
*Menu option 22, brief report 17, owner Julia*

As a policy researcher in the government relations team, I want to see every capital city in the world ordered by population, so that I can decide which capitals to prioritise for briefings.

- [ ] When I choose option 22, then 232 capital cities are listed. The 7 countries with no recorded capital are left out, with no error.
- [ ] Each row shows Name, Country and Population, in that order.
- [ ] The first three rows are Seoul (South Korea, 9,981,619), Jakarta (Indonesia, 9,604,900) and Ciudad de México (Mexico, 8,591,309).
- [ ] A capital is found through the country's Capital reference (`country.Capital = city.ID`), not by name. For example, London in Canada must not appear.

#### US03.2: All capital cities in a continent, largest population first (#147)
*Menu option 23, brief report 18, owner Julia*

As a policy researcher in the government relations team, I want to see every capital city in a continent I choose, ordered by population, so that I can plan which capitals in that continent to cover.

- [ ] When I choose option 23 and enter `Europe`, then 46 capital cities are listed.
- [ ] The first three rows are Moscow (8,389,200), London (7,285,000) and Berlin (3,386,667).
- [ ] When I enter `Atlantis`, then `No capital cities found. Check the name is spelled correctly.` is printed.

#### US03.3: All capital cities in a region, largest population first (#148)
*Menu option 24, brief report 19, owner Julia*

As a policy researcher in the government relations team, I want to see every capital city in a region I choose, ordered by population, so that I can compare the capitals of neighbouring countries.

- [ ] When I choose option 24 and enter `Caribbean`, then 24 capital cities are listed.
- [ ] The first three rows are La Habana (2,256,000), Santo Domingo de Guzmán (1,609,966) and Port-au-Prince (884,472).
- [ ] When I enter `Middle Earth`, then `No capital cities found. Check the name is spelled correctly.` is printed.

#### US03.4: Top N capital cities in the world (#149)
*Menu option 25, brief report 20, owner Julia*

As a policy researcher in the government relations team, I want to see the N most populated capital cities in the world, with N chosen by me, so that I can shortlist capitals for a visit programme.

- [ ] When I choose option 25 and enter N = 3, then exactly Seoul, Jakarta and Ciudad de México are shown, in that order.
- [ ] When I enter N = 500, then all 232 capitals are shown and no error is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US03.5: Top N capital cities in a continent (#150)
*Menu option 26, brief report 21, owner Julia*

As a policy researcher in the government relations team, I want to see the N most populated capitals in a continent I choose, so that I can shortlist the main capitals of that continent.

- [ ] When I choose option 26, enter `Europe` and N = 2, then exactly Moscow then London are shown.
- [ ] When I enter `Atlantis` and N = 3, then `No capital cities found. Check the name is spelled correctly.` is printed.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

#### US03.6: Top N capital cities in a region (#151)
*Menu option 27, brief report 22, owner Julia*

As a policy researcher in the government relations team, I want to see the N most populated capitals in a region I choose, so that I can shortlist the main capitals of that region.

- [ ] When I choose option 27, enter `Caribbean` and N = 3, then exactly La Habana, Santo Domingo de Guzmán and Port-au-Prince are shown, in that order.
- [ ] When I enter `British Islands` and N = 5, then only London (7,285,000) and Dublin (481,854) are shown, with no error.
- [ ] Invalid values for N (`0`, `-5`, `abc`, `2.5`) print `N must be a whole number greater than 0` and ask again.

### US04 - City and non-city population breakdown (epic #16)

#### US04.1: City and non-city population for each continent (#152)
*Menu option 29, brief report 23, owner Zain*

As a regional development analyst, I want to see, for each continent, the total population and how many people live in and outside cities, so that I can see how urbanised each continent is when planning rural and urban programmes.

- [ ] When I choose option 29, then 7 rows are shown, one per continent, largest total first.
- [ ] Each row shows the continent name, total population, population in cities with %, and population not in cities with %.
- [ ] Asia: total 3,705,025,700; in cities 697,604,103 (18.83%); not in cities 3,007,421,597 (81.17%).
- [ ] Europe: total 730,074,600; in cities 241,942,813 (33.14%); not in cities 488,131,787 (66.86%).
- [ ] Each country is counted once: a continent's total equals the sum of its countries' populations, not that sum multiplied by its number of cities.
- [ ] Antarctica (population 0) shows 0.00% and 0.00%, with no division by zero.

#### US04.2: City and non-city population for each region (#153)
*Menu option 30, brief report 24, owner Zain*

As a regional development analyst, I want to see, for each region, the total population and how many people live in and outside cities, so that I can compare urbanisation between regions.

- [ ] When I choose option 30, then 25 rows are shown, one per region, largest total first.
- [ ] Eastern Asia is first: total 1,507,328,000; in cities 317,476,534 (21.06%); not in cities 1,189,851,466 (78.94%).
- [ ] Caribbean: total 38,140,000; in cities 11,067,550 (29.02%); not in cities 27,072,450 (70.98%).
- [ ] For every region with a population above 0, city % + non-city % = 100.00%.

#### US04.3: City and non-city population for each country (#154)
*Menu option 31, brief report 25, owner Zain*

As a regional development analyst, I want to see, for each country, the total population and how many people live in and outside cities, so that I can find the least urbanised countries for rural programmes.

- [ ] When I choose option 31, then 239 rows are shown, largest total first.
- [ ] China is first: total 1,277,558,000; in cities 175,953,614 (13.77%); not in cities 1,101,604,386 (86.23%).
- [ ] United Kingdom: total 59,623,400; in cities 22,436,673 (37.63%); not in cities 37,186,727 (62.37%).
- [ ] The 7 countries with population 0 show 0.00% and 0.00%, with no error.
- [ ] Countries whose recorded city population is larger than the country population (Singapore, Gibraltar, Cocos (Keeling) Islands) are shown as agreed in #118.

### US05 - Population totals (epic #17)

#### US05.1: Population of the world (#155)
*Menu option 17, brief lookup: world, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of the world as one figure, so that I can answer an enquiry without running a full report.

- [ ] When I choose option 17, then the world population 6,078,749,450 is shown next to a label saying it is the world.
- [ ] The value is held in a `long`, because it is larger than the `int` maximum of 2,147,483,647, and it prints without overflow.

#### US05.2: Population of a continent (#156)
*Menu option 32, brief lookup: continent, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of a continent I name, so that I can answer continent enquiries quickly.

- [ ] When I choose option 32 and enter `Europe`, then `Europe` and 730,074,600 are shown.
- [ ] When I enter `Atlantis`, then `No matching places found. Check the name and country.` is printed.

#### US05.3: Population of a region (#157)
*Menu option 33, brief lookup: region, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of a region I name, so that I can answer regional enquiries quickly.

- [ ] When I choose option 33 and enter `Caribbean`, then 38,140,000 is shown.
- [ ] When I enter `British Islands`, then 63,398,500 is shown (United Kingdom plus Ireland).
- [ ] When I enter `Middle Earth`, then `No matching places found. Check the name and country.` is printed.

#### US05.4: Population of a country (#158)
*Menu option 34, brief lookup: country, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of a country I name, so that I can answer national enquiries quickly.

- [ ] When I choose option 34 and enter `United Kingdom`, then 59,623,400 is shown.
- [ ] When I enter `Narnia`, then `No matching places found. Check the name and country.` is printed.

#### US05.5: Population of a district in a country (#159)
*Menu option 35, brief lookup: district, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of a district in a country I name, so that I can answer local enquiries without mixing up districts that share a name.

- [ ] When I choose option 35, I am asked for the district and then the country, because 23 district names are used in more than one country (for example `Distrito Federal` in Argentina, Brazil, Mexico and Venezuela).
- [ ] District `Scotland` in `United Kingdom` shows `United Kingdom - Scotland` and 1,429,620, the sum of its 4 recorded cities (Glasgow, Edinburgh, Aberdeen, Dundee).
- [ ] District `Distrito Federal` in `Mexico` counts only Mexico's cities.
- [ ] The output or the README says that district population is calculated from recorded cities only, so it is not mistaken for the full district population.
- [ ] District `Scotland` in `France` prints `No matching places found. Check the name and country.`

#### US05.6: Population of a city in a country (#160)
*Menu option 36, brief lookup: city, owner Kyle*

As a statistics officer who answers population enquiries, I want to get the population of a city in a country I name, so that I can answer enquiries about one city without confusing it with another city of the same name.

- [ ] When I choose option 36, I am asked for the city and then the country.
- [ ] `Edinburgh` in `United Kingdom` gives 450,180.
- [ ] `Hamilton` in `Canada` gives only 335,614, not Hamilton in Bermuda or New Zealand.
- [ ] `Richmond` in `United States` matches two cities, so the menu prints `Multiple cities found:`, lists `United States - Richmond (Virginia)` and `United States - Richmond (California)`, and asks me to choose one. The chosen one is shown (Virginia 197,790 or California 94,100).
- [ ] `Edinburgh` in `France` prints `No matching places found. Check the name and country.`

### US06 - Language statistics (epic #18)

#### US06.1: Speakers of Chinese, English, Hindi, Spanish and Arabic (#161)
*Menu option 28, brief language report, owner Kyle*

As a language services manager, I want to see the estimated number of speakers of Chinese, English, Hindi, Spanish and Arabic and their share of the world population, so that I can decide which languages our translated reports are produced in first.

- [ ] When I choose option 28, then exactly 5 rows are shown, ordered by speakers, largest first.
- [ ] The values are: Chinese 1,191,843,539 (19.61%); Hindi 405,633,070 (6.67%); Spanish 355,029,462 (5.84%); English 347,077,867 (5.71%); Arabic 233,839,239 (3.85%).
- [ ] Speakers in a country = country population x language percentage / 100, summed over all countries and rounded to a whole number.
- [ ] Percentage = speakers / world population (6,078,749,450) x 100, shown to 2 decimal places.
- [ ] The columns are Language, Speakers and % of world population.

## How a story is accepted

1. The tests listed in the story's issue pass with `mvn verify`, locally and in GitHub Actions.
2. A teammate reviews and approves the pull request.
3. The Product Owner checks every acceptance criterion is met before the issue is closed (see `docs/DEFINITION_OF_DONE.md`).
