package com.group_nine.mandatory_one.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group_nine.mandatory_one.FakePersonGenerator;
import com.group_nine.mandatory_one.FakePersonService;
import com.group_nine.mandatory_one.model.Address;
import com.group_nine.mandatory_one.model.FakePerson;
import com.group_nine.mandatory_one.model.PersonName;
import com.group_nine.mandatory_one.providers.PersonNameProvider;
import com.group_nine.mandatory_one.providers.TownProvider;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FakePersonServiceTest {

  @Mock private Random random;
  @Mock private PersonNameProvider personNameProvider;
  @Mock private TownProvider townProvider;
  @Mock private FakePersonGenerator generator;

  @InjectMocks private FakePersonService service;

  private PersonName testPerson;

  @BeforeEach
  void setUp() {
    testPerson = new PersonName("John", "Doe", "male");
  }

  @Test
  @DisplayName("generateFakePerson orchestrates generator and providers")
  void generateFakePersonWorksCorrectly() {
    // Arrange
    when(personNameProvider.getRandomPerson(random)).thenReturn(testPerson);
    when(generator.generateBirthDate(eq(random))).thenReturn("1990-01-01");
    Address mockAddress = new Address("Main St", "1", "st", "th", "1000", "City");
    when(generator.generateAddress(eq(random), eq(townProvider))).thenReturn(mockAddress);
    when(generator.generatePhone(random)).thenReturn("12345678");
    when(generator.generateCpr(eq(random), eq("1990-01-01"), eq("male"))).thenReturn("0101901231");

    // Act
    FakePerson result = service.generateFakePerson();

    // Assert
    assertNotNull(result);
    assertEquals("John", result.firstName());
    assertEquals("0101901231", result.cpr());
    assertEquals(mockAddress, result.address());

    verify(personNameProvider).getRandomPerson(random);
    verify(generator).generateBirthDate(random);
    verify(generator).generateAddress(random, townProvider);
    verify(generator).generatePhone(random);
    verify(generator).generateCpr(random, "1990-01-01", "male");
  }

  @Test
  @DisplayName("generateFakePersons returns expected amount")
  void generateFakePersonsReturnsCorrectAmount() {
    // Arrange
    when(personNameProvider.getRandomPerson(random)).thenReturn(testPerson);
    when(generator.generateBirthDate(any())).thenReturn("1990-01-01");
    when(generator.generateAddress(any(), any())).thenReturn(mock(Address.class));
    when(generator.generatePhone(any())).thenReturn("12345678");
    when(generator.generateCpr(any(), any(), any())).thenReturn("0101901231");

    // Act
    List<FakePerson> result = service.generateFakePersons(5);

    // Assert
    assertEquals(5, result.size());
    verify(generator, times(5)).generateBirthDate(any());
  }

  @Test
  @DisplayName("generateFakePersons throws exception for invalid amount")
  void generateFakePersonsInvalidAmountThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> service.generateFakePersons(0));
    assertThrows(IllegalArgumentException.class, () -> service.generateFakePersons(101));
  }
}
