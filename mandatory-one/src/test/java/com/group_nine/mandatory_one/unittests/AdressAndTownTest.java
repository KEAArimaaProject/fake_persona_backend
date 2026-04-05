package com.group_nine.mandatory_one.unittests;

import org.junit.jupiter.api.Test;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;

import com.group_nine.mandatory_one.FakePersonGenerator;

class AdressAndTownTest {
    private FakePersonGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new FakePersonGenerator();
    }

    @Test
    @DisplayName("shouldReturnSt_whenRandomLessThan3")
    void shouldReturnSt_whenRandomLessThan3() {
        Random random = mock(Random.class);

        when(random.nextInt(10)).thenReturn(2);

        String result = generator.generateFloor(random);

        assertEquals("st", result);
    }

    @Test
    @DisplayName("should return number when more than 3")
    void testGenerateFloor_number() {
        Random random = mock(Random.class);

        when(random.nextInt(10)).thenReturn(5);
        when(random.nextInt(99)).thenReturn(42);

        String result = generator.generateFloor(random);

        assertEquals("43", result);
    }

    @Test
    @DisplayName("Should return TH when lower than 8")
    void testGenerateDoor_th(){
        Random random = mock(Random.class);

        when(random.nextInt(20)).thenReturn(5); //+1

        String result = generator.generateDoor(random);

        assertEquals("th", result);
    }

    @Test
    @DisplayName("Should return 'tv' when lower than 15 & higher than 8")
    void testGenerateDoor_tv(){
        Random random = mock(Random.class);

        when(random.nextInt(20)).thenReturn(11); // +1

        String result = generator.generateDoor(random);

        assertEquals("tv", result);
    }


    @Test
    @DisplayName("Should return 'mf' when lower than 17 & higher than 15")
    void testGenerateDoor_mf(){
        Random random = mock(Random.class);

        when(random.nextInt(20)).thenReturn(15); // +1

        String result = generator.generateDoor(random);

        assertEquals("mf", result);
    }

    @Test
    @DisplayName("Should return a letter + number without a dash if the value is 19")
    void testGenerateDoor_random_is_19(){
        Random random = mock(Random.class);

        when(random.nextInt(anyInt())).thenReturn(
                18,  // -> doorType +1 = 19
                2,   // -> letter index → 'c'
                104  // -> number = 105
        );
        String result = generator.generateDoor(random);

        assertEquals("c105", result);
    }


    @Test
    @DisplayName("Should return a letter + number with a dash if value is 20")
    void testGenerateDoor_dash(){
        Random random = mock(Random.class);

        when(random.nextInt(anyInt())).thenReturn(
                19,  // -> doorType + 1 = 20
                2,   // -> letter index → 'c'
                104  // -> number + 1 = 105
        );

        String result = generator.generateDoor(random);

        assertEquals("c-105", result);
    }

    // class end
}