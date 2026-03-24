package com.group_nine.mandatory_one.unittests;

import com.group_nine.mandatory_one.FakePersonGenerator;
import com.group_nine.mandatory_one.FakePersonService;
import com.group_nine.mandatory_one.model.Town;
import com.group_nine.mandatory_one.providers.JsonPersonNameProvider;
import com.group_nine.mandatory_one.providers.TownProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FakeInfo – name and gender generation (Integration)")
class NameAndGenderTests {

    private static final Town FIXED_TOWN = new Town("1234", "TestTown");

    @Test
    @DisplayName("generateNameAndGender returns a well-formed person without using the database")
    void generateNameAndGenderReturnsWellFormedPerson() {
        Random random = new Random(42);
        FakePersonGenerator generator = new FakePersonGenerator();

        TownProvider townProvider = () -> FIXED_TOWN;

        FakePersonService service = new FakePersonService(
                random,
                new JsonPersonNameProvider(),
                townProvider,
                generator
        );

        var result = service.generateNameAndGender();

        assertNotNull(result);
        assertNotNull(result.firstName());
        assertNotNull(result.lastName());
        assertNotNull(result.gender());

        assertFalse(result.firstName().isBlank());
        assertFalse(result.lastName().isBlank());

        assertTrue(result.gender().equals("male") || result.gender().equals("female"),
                "gender must be male or female, was: " + result.gender());
    }
}