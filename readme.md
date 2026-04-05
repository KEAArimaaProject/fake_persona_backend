Programming language: Java springboot

Fronted at https://github.com/KEAArimaaProject/fake_persona_frontend

This project is primarily about tests, not code.
Here is a file with some thoughts about 
how to handle tests with random values:

src/test/java/com/group_nine/mandatory_one/notesForTheTeam.txt

## CI + formatter + tests (mandatory-one)

Run commands from the `mandatory-one/` folder.

## Git hooks (Husky) - formatting + unit tests on commit

We use Husky (v9+) to run formatting + unit tests locally on every commit.

### Install hooks (first time after clone)

From repo root:

- `npm install`

### Setup / enable Husky (v9+)

From repo root:

- Ensure `package.json` has: `scripts.prepare = "husky"`
- Run: `npx husky init`

### pre-commit hook content

`.husky/pre-commit` should run:

- `cd mandatory-one && ./mvnw -q spotless:apply && ./mvnw -q checkstyle:check && ./mvnw -q test`

### Format Java (google-java-format)

- Auto-format:
  - `./mvnw spotless:apply`
- Check formatting (same as CI):
  - `./mvnw spotless:check`

### Lint (Checkstyle)

- Run linter (same as CI):
  - `./mvnw checkstyle:check`

### Run tests

- Unit + integration (requires MySQL):
  - Start DB: `docker compose -f db/docker-compose.yml up -d`
  - Run tests: `./mvnw test`
  - Stop DB: `docker compose -f db/docker-compose.yml down`

### Test coverage (JaCoCo)

From `mandatory-one/`, run:

| Goal | Command |
|------|---------|
| Unit tests and generate the HTML/XML coverage report | `./mvnw test jacoco:report` |
| Full `verify` (includes `*IT` integration tests) and generate the report | `./mvnw verify` (start MySQL first if integration tests need it) |
| Regenerate the report using the last test run’s data | `./mvnw jacoco:report` |

**View the report**

Open `mandatory-one/target/site/jacoco/index.html` in a web browser (for example double-click the file in File Explorer). Do not rely on the editor preview; the HTML is minified into one line.

**Other outputs**

- `target/site/jacoco/jacoco.xml` — machine-readable, useful for CI or SonarQube.  
- `target/site/jacoco/jacoco.csv` — tabular summary.
