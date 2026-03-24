package com.group_nine.mandatory_one.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.group_nine.mandatory_one.model.PersonName;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;

@Component
public class JsonPersonNameProvider implements PersonNameProvider {

    private final Path jsonPath;
    private final ObjectMapper objectMapper;

    public JsonPersonNameProvider() {
        this("data/person-names.json");
    }

    public JsonPersonNameProvider(String jsonPath) {
        this(Path.of(jsonPath), new ObjectMapper());
    }

    public JsonPersonNameProvider(Path jsonPath, ObjectMapper objectMapper) {
        this.jsonPath = Objects.requireNonNull(jsonPath, "jsonPath must not be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    @Override
    public PersonName getRandomPerson(Random random) {
        Objects.requireNonNull(random, "random must not be null");

        try {
            byte[] jsonBytes = Files.readAllBytes(jsonPath);
            JsonNode root = objectMapper.readTree(jsonBytes);
            JsonNode persons = root.get("persons");

            if (persons == null || !persons.isArray() || persons.isEmpty()) {
                throw new IllegalStateException("No persons found in JSON: " + jsonPath);
            }

            int idx = random.nextInt(persons.size());
            JsonNode person = persons.get(idx);

            return new PersonName(
                    person.get("firstName").asText(),
                    person.get("lastName").asText(),
                    person.get("gender").asText()
            );
        } catch (IOException e) {
            throw new RuntimeException("Unable to read person names JSON", e);
        }
    }
}