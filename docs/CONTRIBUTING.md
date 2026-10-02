# How we work (SET08103 Group 4)

This explains our workflow step by step. It follows our Code of Conduct.

## The branches
`feature/*` -> `develop` -> `release` -> `main` (then `main` is merged back into `develop`)

| Branch | What it is |
|---|---|
| `feature/issue-N-name` | Your own workspace for one issue |
| `develop` | Where finished features come together |
| `release` | A frozen copy of `develop` we check before calling it a version |
| `main` | The official, working version that gets submitted and marked |

Rule: nobody commits directly to `main` or `develop`. Everything goes in through a Pull Request (PR).

## Doing an issue, step by step
1. **Pick an issue** on the project board, assign it to yourself and move it to **In progress**.
2. **Get the latest `develop`:**
   ```
   git checkout develop
   git pull
   ```
3. **Create your branch:**
   ```
   git checkout -b feature/issue-8-top-n-countries
   ```
4. **Work and commit small and often:**
   ```
   git status
   git add path/to/the/file
   git commit -m "Add top N countries query"
   ```
   Add the files you changed by name (not `git add .`), so each commit holds one change and no IDE or build files sneak in.
5. **Push your branch:**
   ```
   git push -u origin feature/issue-8-top-n-countries
   ```
6. **Open a Pull Request into `develop`** on GitHub. Fill in the template and write `Closes #8` so the PR is linked to the issue. Ask a teammate to review. Move the issue to **In review**.
7. **Fix review comments** by committing to the same branch. The PR updates automatically.
8. **Merge** once approved and delete the branch. Then close the issue and move it to **Done** on the board. GitHub only closes issues automatically when a PR is merged into the default branch (`main`), and we merge into `develop`, so closing it is a manual step.

## Good commit messages
- Start with a verb: "Add", "Fix", "Rename", "Remove"
- Say what and why in one line: `Add N/A for countries without a capital`
- Not: "update", "stuff", "final", "asdf"

## Comments in code (graded!)
Every class and method gets a short Javadoc comment. Add a line comment where the idea is not obvious.
```java
/**
 * Gets all countries ordered from largest to smallest population.
 *
 * @return list of countries, empty if the query fails
 */
public ArrayList<Country> getCountries()
{
    // LEFT JOIN so countries without a capital still appear
    String query = "SELECT ... LEFT JOIN city ci ON c.Capital = ci.ID ...";
}
```

## How to review a Pull Request
1. Open the PR > **Files changed**.
2. Read the code. Does it do what the issue's acceptance criteria say?
3. Check: comments present, names clear, no passwords or IDE files, builds locally.
4. Click the **+** next to a line to leave a comment. Be kind and specific ("could we read this host from an environment variable?").
5. Press **Review changes** > **Approve**, or **Request changes** if something must change first.
A PR with a real review is evidence of teamwork, so everyone should review at least one PR.

## Our sprint routine
- **Planning (start of sprint, ~15 min):** pick issues from the backlog, give them points, assign owners, set the sprint on the board.
- **Stand-up (once a week, 10 min on Teams):** what I did, what I'll do, any blockers.
- **Review (end of sprint):** what met the Definition of Done? Show it running.
- **Retrospective (right after):** what went well, what to improve, one action for next sprint.
- Write notes in `docs/sprints/sprint-N.md` (use the template) and commit them with a PR.

## Project board
- Columns: Backlog -> Ready -> In progress -> In review -> Done
- Each issue has an owner, an estimate (story points) and a sprint
- Keep the board up to date: it is graded evidence

## Where to ask for help
Post in the Teams chat. Nobody minds questions, and Git is easier when we learn it together.
