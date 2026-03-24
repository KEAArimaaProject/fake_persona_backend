package com.group_nine.mandatory_one;

import com.group_nine.mandatory_one.model.*;
import com.group_nine.mandatory_one.providers.PersonNameProvider;
import com.group_nine.mandatory_one.providers.TownProvider;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class FakePersonService {

    public static final int MIN_BULK_PERSONS = 2;
    public static final int MAX_BULK_PERSONS = 100;

    private final Random random;
    private final PersonNameProvider personNameProvider;
    private final TownProvider townProvider;
    private final FakePersonGenerator generator;
    private final Clock clock;

    public FakePersonService(
            Random random,
            PersonNameProvider personNameProvider,
            TownProvider townProvider,
            FakePersonGenerator generator
    ) {
        this.random = Objects.requireNonNull(random, "random must not be null");
        this.personNameProvider = Objects.requireNonNull(personNameProvider, "personNameProvider must not be null");
        this.townProvider = Objects.requireNonNull(townProvider, "townProvider must not be null");
        this.generator = Objects.requireNonNull(generator, "generator must not be null");
        this.clock = Clock.systemDefaultZone();
    }

    public String generateCpr() {
        String birthDate = generator.generateBirthDate(random);
        String gender = personNameProvider.getRandomPerson(random).gender();
        return generator.generateCpr(random, birthDate, gender);
    }

    public PersonName generateNameAndGender() {
        return personNameProvider.getRandomPerson(random);
    }

    public NameGenderDob generateNameGenderAndBirthDate() {
        PersonName person = generateNameAndGender();
        return new NameGenderDob(person.firstName(), person.lastName(), person.gender(), generateBirthDate());
    }

    public CprNameGender generateCprNameAndGender() {
        PersonName person = generateNameAndGender();
        String birthDate = generateBirthDate();
        String cpr = generator.generateCpr(random, birthDate, person.gender());
        return new CprNameGender(cpr, person.firstName(), person.lastName(), person.gender());
    }

    public CprNameGenderDob generateCprNameGenderAndBirthDate() {
        PersonName person = generateNameAndGender();
        String birthDate = generateBirthDate();
        String cpr = generator.generateCpr(random, birthDate, person.gender());
        return new CprNameGenderDob(cpr, person.firstName(), person.lastName(), person.gender(), birthDate);
    }

    public FakePerson generateFakePerson() {
        PersonName person = personNameProvider.getRandomPerson(random);
        String birthDate = generator.generateBirthDate(random);
        Address address = generator.generateAddress(random, townProvider);
        String phoneNumber = generator.generatePhone(random);
        String cpr = generator.generateCpr(random, birthDate, person.gender());

        return new FakePerson(
                cpr,
                person.firstName(),
                person.lastName(),
                person.gender(),
                birthDate,
                address,
                phoneNumber
        );
    }

    public List<FakePerson> generateFakePersons(int amount) {
        if (amount < 1 || amount > MAX_BULK_PERSONS) {
            throw new IllegalArgumentException("Amount must be between 1 and " + MAX_BULK_PERSONS);
        }

        List<FakePerson> persons = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            persons.add(generateFakePerson());
        }
        return persons;
    }

    public Address generateAddress() {
        return generator.generateAddress(random, townProvider);
    }

    public String generatePhone() {
        return generator.generatePhone(random);
    }

    public String generateBirthDate() {
        return generator.generateBirthDate(random);
    }
}
