package com.group_nine.mandatory_one.integrationtests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_nine.mandatory_one.model.PersonName;
import com.group_nine.mandatory_one.providers.JsonPersonNameProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonPersonNameProvider – name and gender data")
class JsonPersonNameProviderTest {

    private static final String PERSON_NAMES_PATH = "data/person-names.json";
    private static final Set<String> VALID_GENDERS = Set.of("male", "female");
    private static JsonNode personsNode;

    @BeforeAll
    static void loadPersonsData() throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(PERSON_NAMES_PATH));
        JsonNode root = new ObjectMapper().readTree(bytes);
        personsNode = root.get("persons");
        assertNotNull(personsNode, "persons array missing in JSON");
    }

    @Test
    @DisplayName("JSON data contains valid entries")
    void jsonDataIsValid() {
        assertTrue(personsNode.size() > 0, "persons list should not be empty");

        for (int i = 0; i < personsNode.size(); i++) {
            JsonNode p = personsNode.get(i);
            assertTrue(p.has("firstName"), "missing firstName at index " + i);
            assertTrue(p.has("lastName"),  "missing lastName at index " + i);
            assertTrue(p.has("gender"),    "missing gender at index " + i);

            String gender = p.get("gender").asText();
            assertTrue(VALID_GENDERS.contains(gender),
                    "invalid gender '%s' at index %d".formatted(gender, i));
        }
    }

    @ParameterizedTest(name = "[{index}] {0} → {1} {2} ({3})")
    @CsvSource({
            "0, Annemette P., Nilsson,   female",
            "1, Freja O.,    Thygesen,   female",
            "42, David J.,    Østergaard, male",
    })
    @DisplayName("returns correct person when index is forced")
    void returnsExpectedPersonWhenIndexIsForced(
            int forcedIndex,
            String expFirst,
            String expLast,
            String expGender
    ) {
        Random fixedRandom = new Random() {
            @Override
            public int nextInt(int bound) {
                if (bound == personsNode.size()) {
                    return forcedIndex % bound;
                }
                return super.nextInt(bound);
            }
        };

        JsonPersonNameProvider provider = new JsonPersonNameProvider();
        PersonName result = provider.getRandomPerson(fixedRandom);

        assertAll(
                () -> assertEquals(expFirst,  result.firstName(), "firstName"),
                () -> assertEquals(expLast,   result.lastName(),  "lastName"),
                () -> assertEquals(expGender, result.gender(),    "gender")
        );
    }
}
