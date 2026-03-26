Programming language: Java springboot

Fronted at https://github.com/KEAArimaaProject/fake_persona_frontend

This project is primarily about tests, not code.
Here is a file with some thoughts about 
how to handle tests with random values:

src/test/java/com/group_nine/mandatory_one/notesForTheTeam.txt

## CI + formatter + tests (mandatory-one)

Run commands from the `mandatory-one/` folder.

### Format Java (google-java-format)

- Auto-format:
  - `./mvnw spotless:apply`
- Check formatting (same as CI):
  - `./mvnw spotless:check`

### Run tests

- Unit + integration (requires MySQL):
  - Start DB: `docker compose -f db/docker-compose.yml up -d`
  - Run tests: `./mvnw test`
  - Stop DB: `docker compose -f db/docker-compose.yml down`
