package com.group_nine.mandatory_one.model;

public record FakePerson(
    String cpr,
    String firstName,
    String lastName,
    String gender,
    String birthDate,
    Address address,
    String phoneNumber) {}
