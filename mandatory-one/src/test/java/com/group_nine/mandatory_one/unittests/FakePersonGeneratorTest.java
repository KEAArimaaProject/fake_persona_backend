package com.group_nine.mandatory_one.unittests;

import com.group_nine.mandatory_one.FakePersonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
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
    void generateBirthDate_returnsValidDate() {
        for (int i = 0; i < 100; i++) {
            String birthDate = generator.generateBirthDate(random);
            assertNotNull(birthDate);
            assertTrue(birthDate.matches("\\d{4}-\\d{2}-\\d{2}"));
            
            int year = Integer.parseInt(birthDate.substring(0, 4));
            assertTrue(year >= 1900 && year <= 2024, "Year should be between 1900 and 2024, was: " + year);
        }
    }

    @Test
    @DisplayName("generateCpr should return valid formatted CPR")
    void generateCpr_returnsFormattedString() {
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
    void generatePhone_returnsValidPhone() {
        for (int i = 0; i < 50; i++) {
            String phone = generator.generatePhone(random);
            assertEquals(8, phone.length());
            // Simple check that it's all digits
            assertTrue(phone.matches("\\d{8}"), "Phone should be 8 digits: " + phone);
        }
    }
}
