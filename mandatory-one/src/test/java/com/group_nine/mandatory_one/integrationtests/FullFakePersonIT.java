package com.group_nine.mandatory_one.integrationtests;

import com.group_nine.mandatory_one.FakePersonGenerator;
import com.group_nine.mandatory_one.FakePersonService;
import com.group_nine.mandatory_one.model.FakePerson;
import com.group_nine.mandatory_one.providers.DatabaseTownProvider;
import com.group_nine.mandatory_one.providers.JsonPersonNameProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FullFakePersonIT – End-to-end integration test")
class FullFakePersonIT {

    private FakePersonService service;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        service = new FakePersonService(
                random,
                new JsonPersonNameProvider(),
                new DatabaseTownProvider(
                        random,
                        "jdbc:mysql://localhost:3307/fakepersonadb",
                        "root",
                        "123456"
                ),
                new FakePersonGenerator()
        );
    }

    @Test
    @DisplayName("generateFakePerson returns a complete and valid person using all real providers")
    void generateFakePersonReturnsCompletePerson() {
        FakePerson person = service.generateFakePerson();

        assertNotNull(person);
        assertNotNull(person.cpr());
        assertNotNull(person.firstName());
        assertNotNull(person.lastName());
        assertNotNull(person.gender());
        assertNotNull(person.birthDate());
        assertNotNull(person.address());
        assertNotNull(person.phoneNumber());

        // Basic format validations
        assertEquals(10, person.cpr().length(), "CPR should be 10 digits");
        assertTrue(person.gender().equals("male") || person.gender().equals("female"));
        assertFalse(person.firstName().isBlank());
        assertFalse(person.lastName().isBlank());
        
        // Address validation
        assertNotNull(person.address().street());
        assertNotNull(person.address().number());
        assertNotNull(person.address().postalCode());
        assertNotNull(person.address().townName());
        
        assertFalse(person.address().postalCode().isBlank());
        assertFalse(person.address().townName().isBlank());

        System.out.println("[DEBUG_LOG] Generated Full Person: " + person.firstName() + " " + person.lastName() + 
                           ", CPR: " + person.cpr() + ", Town: " + person.address().townName());
    }

    @Test
    @DisplayName("generateFakePersons returns requested amount of valid persons")
    void generateFakePersonsReturnsCorrectAmount() {
        int amount = 5;
        var persons = service.generateFakePersons(amount);

        assertEquals(amount, persons.size());
        for (FakePerson p : persons) {
            assertNotNull(p.cpr());
            assertNotNull(p.address().townName());
        }
    }

    @Test
    @DisplayName("generateCprNameGenderAndBirthDate returns consistent data")
    void generateCprNameGenderAndBirthDateIsConsistent() {
        var result = service.generateCprNameGenderAndBirthDate();

        assertNotNull(result);
        assertEquals(10, result.cpr().length());
        assertFalse(result.firstName().isBlank());
        assertNotNull(result.birthDate());
        
        // Ensure CPR birthdate part matches DOB
        String cprPrefix = result.cpr().substring(0, 6);
        // ISO: 1990-01-05 -> DDMMYY = 050190
        String isoDate = result.birthDate();
        String day = isoDate.substring(8, 10);
        String month = isoDate.substring(5, 7);
        String year = isoDate.substring(2, 4);
        String expectedCprPrefix = day + month + year;
        
        assertEquals(expectedCprPrefix, cprPrefix, "CPR prefix should match birthdate");
    }
}
