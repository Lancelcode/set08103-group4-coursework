# CR1 repo status audit

Snapshot taken 2 Oct 2026, comparing every branch against the CR1 checklist.

## Branches

| Branch | Contains |
|---|---|
| `main` (default) | workflow, README, user stories, LICENSE |
| `develop` | `pom.xml`, `Dockerfile`, workflow, `.gitignore`, Hello World `App.java` |
| `release` | `CODE_OF_CONDUCT.md`, backlog |
| `feature/issue-7-country-population` | `Country.java`, JDBC `App.java`, `docker-compose.yml`, `db/world.sql` |

## CR1 checklist

| Item | Status | Notes |
|---|---|---|
| GitHub project set up | Done | |
| Product Backlog | Partial | Plain text file, only on `release` |
| Maven self-contained JAR | Not done | No jar/shade plugin or main-class manifest; workflow runs `mvn compile` only |
| Dockerfile works | Partial | Runs compiled classes, not a JAR |
| GitHub Actions using JAR and Docker | Partial | Builds and runs Docker; no JAR step |
| master / develop / release branches | Mismatch | Default branch is `main`, brief says `master` |
| First release on GitHub | Not done | No tags |
| Code of Conduct | Partial | Only on `release` |

## Gaps to fix

1. Merge the Code of Conduct and backlog so the reviewer sees them.
2. Two conflicting `pom.xml` files (MongoDB on `develop`, MySQL on the feature branch). Keep MySQL.
3. Add the Maven jar plugin and change the Dockerfile and workflow to use the JAR.
4. Create the first GitHub release.
5. Set IntelliJ SDK to JDK 17 and stop tracking `.idea/`.