package com.group_nine.mandatory_one.unittests;

import com.group_nine.mandatory_one.FakePersonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class FakePersonGeneratorTest {

    private FakePersonGenerator generator;
    private Clock fixedClock;
    private Random random;

    @BeforeEach
    void setUp() {
        // Fixed clock at 2024-03-23T12:00:00Z
        fixedClock = Clock.fixed(Instant.parse("2024-03-23T12:00:00Z"), ZoneId.of("UTC"));
        generator = new FakePersonGenerator(fixedClock);
        random = new Random(42); // Seeded random for reproducibility
    }

    @Test
    @DisplayName("generateBirthDate should return date within expected range")
    void generateBirthDateReturnsValidDate() {

        String birthDate = generator.generateBirthDate(random);
        assertNotNull(birthDate);
        assertTrue(birthDate.matches("\\d{4}-\\d{2}-\\d{2}"));

        int year = Integer.parseInt(birthDate.substring(0, 4));
        assertTrue(year >= 1900 && year <= 2024, "Year should be between 1900 and 2024, was: " + year);

    }

    @Test
    @DisplayName("generateCpr should return valid formatted CPR")
    void generateCprReturnsFormattedString() {
        String birthDate = "1990-05-15";
        String maleCpr = generator.generateCpr(random, birthDate, "male");
        String femaleCpr = generator.generateCpr(random, birthDate, "female");

        assertEquals(10, maleCpr.length());
        assertEquals(10, femaleCpr.length());
        assertTrue(maleCpr.startsWith("150590"));
        assertTrue(femaleCpr.startsWith("150590"));

        int maleLastDigit = Character.getNumericValue(maleCpr.charAt(9));
        int femaleLastDigit = Character.getNumericValue(femaleCpr.charAt(9));

        assertEquals(1, maleLastDigit % 2, "Male CPR should end in odd digit");
        assertEquals(0, femaleLastDigit % 2, "Female CPR should end in even digit");
    }

    @Test
    @DisplayName("generatePhone should return 8-digit phone number with valid prefix")
    void generatePhoneReturnsValidPhone() {
        String phone = generator.generatePhone(random);

        boolean mandatoryInitials = PHONE_PREFIXES_REQUIREMENT.stream()
                .anyMatch(prefix -> phone.startsWith(prefix));

        boolean allInts = phone.chars().allMatch(Character::isDigit);

        assertEquals(true, mandatoryInitials);
        assertEquals(true, allInts);
        assertEquals(8, phone.length());

    }

    private static final List<String> PHONE_PREFIXES_REQUIREMENT = List.of(
            "2", "30", "31", "40", "41", "42", "50", "51", "52", "53", "60", "61", "71", "81", "91", "92", "93", "342",
            "344", "345", "346", "347", "348", "349", "356", "357", "359", "362", "365", "366", "389", "398", "431",
            "441", "462", "466", "468", "472", "474", "476", "478", "485", "486", "488", "489", "493", "494", "495",
            "496", "498", "499", "542", "543", "545", "551", "552", "556", "571", "572", "573", "574", "577", "579",
            "584", "586", "587", "589", "597", "598", "627", "629", "641", "649", "658", "662", "663", "664", "665",
            "667", "692", "693", "694", "697", "771", "772", "782", "783", "785", "786", "788", "789", "826", "827", "829"
    );
}
