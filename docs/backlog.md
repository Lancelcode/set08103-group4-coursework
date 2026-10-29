# Product backlog - SET08103 Group 4 (population reporting system)

Priorities, sizes and sprints are tracked on the GitHub Project board "Group_4".

## Requirements (epics and their specific user stories)

After the CR2 feedback, each grouped story became an epic and was split into one specific story per report or lookup. Full stories are in [user-stories.md](user-stories.md).

| Epic | Area | Brief | Specific stories |
|---|---|---|---|
| #13 US01 | Country reports: all and Top N, for world / continent / region | reports 1-6 | US01.1 - US01.6 (#130 - #135) |
| #14 US02 | City reports: all and Top N, for world / continent / region / country / district | reports 7-16 | US02.1 - US02.10 (#136 - #145) |
| #15 US03 | Capital city reports: all and Top N, for world / continent / region | reports 17-22 | US03.1 - US03.6 (#146 - #151) |
| #16 US04 | City and non-city population breakdown: per continent / region / country | reports 23-25 | US04.1 - US04.3 (#152 - #154) |
| #17 US05 | Population totals: world, continent, region, country, district, city | 6 lookups | US05.1 - US05.6 (#155 - #160) |
| #18 US06 | Language statistics: Chinese, English, Hindi, Spanish, Arabic | language report | US06.1 (#161) |

## Report columns

| Report | Columns |
|---|---|
| Country | Code, Name, Continent, Region, Population, Capital |
| City | Name, Country, District, Population |
| Capital city | Name, Country, Population |
| Population breakdown | Name, total, in cities (with %), not in cities (with %) |

## Process and delivery

- Self-contained JAR in Docker and CI (#25: #27, #28, #29)
- Use cases and use case diagram (CR2)
- Unit and integration tests, tests in GitHub Actions and coverage (CR3, Sprint 3: #114 - #129)
- Deployment and bug reporting (CR4)
