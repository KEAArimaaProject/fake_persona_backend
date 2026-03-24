package com.group_nine.mandatory_one.providers;

import com.group_nine.mandatory_one.model.PersonName;

import java.util.Random;

public interface PersonNameProvider {
    PersonName getRandomPerson(Random random);
}