# Definition of Done

An issue is **Done** only when ALL of these are true (half-done work is not done, see Unit 01b):

1. All acceptance criteria in the issue are met.
2. The work was done on a `feature/issue-N-...` branch created from `develop`.
3. The code has comments: a Javadoc line on every class and method, and a short comment wherever the logic is not obvious.
4. The project builds with `mvn package` on JDK 17 and the GitHub Actions run is green.
5. Results were checked against the database (for report stories).
6. A Pull Request into `develop` was opened with `Closes #N` in its description.
7. At least one teammate (not the author) reviewed and approved the PR.
8. The issue is closed and moved to **Done** on the project board (this is manual, because PRs into `develop` do not close issues automatically).

When tests exist (from Code Review 3), the new code must also have unit tests, and they must pass in GitHub Actions.
