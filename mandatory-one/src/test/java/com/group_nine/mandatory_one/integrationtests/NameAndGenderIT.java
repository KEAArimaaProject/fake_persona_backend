package com.group_nine.mandatory_one.integrationtests;

import static org.junit.jupiter.api.Assertions.*;

import com.group_nine.mandatory_one.FakePersonGenerator;
import com.group_nine.mandatory_one.FakePersonService;
import com.group_nine.mandatory_one.providers.DatabaseTownProvider;
import com.group_nine.mandatory_one.providers.JsonPersonNameProvider;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameAndGenderIT {

  @Test
  @DisplayName("generateNameAndGender returns a valid person using the real database town provider")
  void generateNameAndGenderReturnsWellFormedPerson() {
    Random random = new Random();

    FakePersonService service =
        new FakePersonService(
            random,
            new JsonPersonNameProvider(),
            new DatabaseTownProvider(
                random, "jdbc:mysql://localhost:3307/fakepersonadb", "root", "123456"),
            new FakePersonGenerator());

    var result = service.generateNameAndGender();

    assertNotNull(result);
    assertNotNull(result.firstName());
    assertNotNull(result.lastName());
    assertNotNull(result.gender());

    assertFalse(result.firstName().isBlank());
    assertFalse(result.lastName().isBlank());

    assertTrue(
        result.gender().equals("male") || result.gender().equals("female"),
        "gender must be male or female, was: " + result.gender());
  }
}
