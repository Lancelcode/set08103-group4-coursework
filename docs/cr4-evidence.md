# Code Review 4 Evidence
 
Everything the CR4 marker needs, with a link to the proof for each item. All figures are from `develop` on 23 Nov 2026, released as [v1.0](https://github.com/Lancelcode/set08103-group4-coursework/releases/tag/v1.0).
 
## CR4 checklist
 
| CR4 item | Evidence |
|---|---|
| Deployment working | Every push to `release` tests, builds and publishes the JAR to the [`latest` release](https://github.com/Lancelcode/set08103-group4-coursework/releases/tag/latest) (#229). Every version tag on `main` publishes its own release with the JAR and notes, for example [v1.0](https://github.com/Lancelcode/set08103-group4-coursework/releases/tag/v1.0) (#244). See [Deployment](#deployment) |
| Bug reporting set up | [Issue templates](https://github.com/Lancelcode/set08103-group4-coursework/issues/new/choose) for bugs and feature requests (#246), priority and severity labels, and the [bugfix process](CONTRIBUTING.md#bugs) (#247). See [Bug reporting](#bug-reporting) |
| GitHub metrics | [Merged PRs in Sprint 4](https://github.com/Lancelcode/set08103-group4-coursework/pulls?q=is%3Apr+is%3Amerged+merged%3A%3E%3D2026-11-19), one issue per task, every member with merged PRs |
| Code quality and comments | PMD runs on every push (#234). All findings were fixed (#240, #243) and the PMD job now fails on any new finding. Public methods have Javadoc comments |
| Branch usage | `feature/*`, `bugfix/*` and `docs/*` -> `develop` -> `release` -> `main`. `main`, `develop` and `release` are protected: every change needs a pull request, one approval and passing checks |
| CI working | Four jobs on every push: Unit tests, Integration tests, Code quality (PMD), then Build and run the app. [Runs on `main`](https://github.com/Lancelcode/set08103-group4-coursework/actions?query=branch%3Amain). Workflow: [`.github/workflows/main.yml`](../.github/workflows/main.yml) |
| Kanban in use | [Group_4 board, Sprint 4 milestone](https://github.com/users/Lancelcode/projects/1/views/1?filterQuery=milestone%3A%22Sprint%204%20-%20CR4%20(deployment)%22). Every issue has an owner, labels and a status |
| Unit test quality and coverage | **70 unit tests** in 10 classes and **93 integration tests** in 18 classes. Sprint 4 added tests for the connection retry and disconnect paths (#236) and the uncovered Menu branches (#241). [Codecov report](https://app.codecov.io/gh/Lancelcode/set08103-group4-coursework) |
| Requirements met | All 25 reports, 6 lookups and the language report, each tested against the World database (see the [CR3 evidence](cr3-evidence.md#user-stories-and-their-tests)) |
 
## Deployment
 
| Step | What happens |
|---|---|
| Push to `release` | The workflow runs the tests, builds the JAR, runs the app against MySQL in Docker, then deletes and recreates the `latest` release on the new commit (#229, #232) |
| Push a `v*` tag on `main` | After the same checks, the `versioned-release` job publishes a new GitHub Release named after the tag, with the JAR and generated release notes (#244) |
| Run a release | Download the JAR, start MySQL with `docker compose up -d mysql` from the repository folder and run `java -jar set08103-group4-jar-with-dependencies.jar` ([README](../README.md#releases), #233) |
 
The [UML deployment diagram](diagrams/deployment-diagram.png) (#249) shows the CI jobs, GitHub Releases and the app and MySQL containers.
 
## Bug reporting
 
| Part | Where |
|---|---|
| Bug report template | [`.github/ISSUE_TEMPLATE/bug_report.md`](../.github/ISSUE_TEMPLATE/bug_report.md): steps to reproduce, expected and actual result, priority and severity |
| Feature request template | [`.github/ISSUE_TEMPLATE/feature_request.md`](../.github/ISSUE_TEMPLATE/feature_request.md) |
| Issue chooser | [`.github/ISSUE_TEMPLATE/config.yml`](../.github/ISSUE_TEMPLATE/config.yml): blank issues are off, so every issue uses a template |
| Labels | `bug`, `priority: high / medium / low`, `severity: blocker / major / minor` |
| Process | [CONTRIBUTING: Bugs](CONTRIBUTING.md#bugs): report, label, `bugfix/issue-<number>-<name>` branch, regression test, PR with `Fixes #<number>` |
| README | [Reporting a bug](../README.md#reporting-a-bug) (#248) |
 
Bugs logged and fixed with this process during Sprint 4:
 
| Bug | Labels | Fixed by |
|---|---|---|
| #231 Workflow file does not parse | `priority: high`, `severity: blocker` | PR #230 |
| #232 `latest` release points to the old commit | `priority: high`, `severity: major` | PR #230 |
| #242 README report count | `priority: low`, `severity: minor` | PR #257 |
 
Bug hunt (#245): every menu option was run in Docker, including invalid input, with no new bugs found.
 
## Sprint evidence
 
[Sprint 4 notes](sprints/sprint-4.md): goal, rotated roles, tasks, check-ins, review, retrospective and contribution.
