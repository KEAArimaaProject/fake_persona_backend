package com.group_nine.mandatory_one;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FakeInfo {

    public static final String GENDER_FEMININE = "female";
    public static final String GENDER_MASCULINE = "male";

    private static final String FILE_PERSON_NAMES = "data/person-names.json";

    private static final List<String> PHONE_PREFIXES = List.of(
            "2", "30", "31", "40", "41", "42", "50", "51", "52", "53", "60", "61", "71", "81", "91", "92", "93", "342",
            "344", "345", "346", "347", "348", "349", "356", "357", "359", "362", "365", "366", "389", "398", "431",
            "441", "462", "466", "468", "472", "474", "476", "478", "485", "486", "488", "489", "493", "494", "495",
            "496", "498", "499", "542", "543", "545", "551", "552", "556", "571", "572", "573", "574", "577", "579",
            "584", "586", "587", "589", "597", "598", "627", "629", "641", "649", "658", "662", "663", "664", "665",
            "667", "692", "693", "694", "697", "771", "772", "782", "783", "785", "786", "788", "789", "826", "827", "829"
    );

    private static final int MIN_BULK_PERSONS = 2;
    private static final int MAX_BULK_PERSONS = 100;

    private static final Random RANDOM = new Random();

    private String cpr;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDate; // ISO yyyy-MM-dd
    private Map<String, Object> address = new HashMap<>();
    private String phone;

    public FakeInfo() {
        setFullNameAndGender();
        setBirthDate();
        setCpr();
        setAddress();
        setPhone();
    }

    private void setCpr() {
        if (birthDate == null) {
            setBirthDate();
        }
        if (firstName == null || lastName == null || gender == null) {
            setFullNameAndGender();
        }

        int finalDigit = RANDOM.nextInt(10);
        if (GENDER_FEMININE.equals(gender) && finalDigit % 2 == 1) {
            finalDigit++;
        }

        StringBuilder sb = new StringBuilder();
        // yyyy-MM-dd
        sb.append(birthDate.substring(8, 10))   // dd
          .append(birthDate.substring(5, 7))   // MM
          .append(birthDate.substring(2, 4))   // yy
          .append(getRandomDigit())
          .append(getRandomDigit())
          .append(getRandomDigit())
          .append(finalDigit);

        this.cpr = sb.toString();
    }

    private void setBirthDate() {
        int year = 1900 + RANDOM.nextInt(LocalDate.now().getYear() - 1900 + 1);
        int month = 1 + RANDOM.nextInt(12);
        int day;

        if (List.of(1, 3, 5, 7, 8, 10, 12).contains(month)) {
            day = 1 + RANDOM.nextInt(31);
        } else if (List.of(4, 6, 9, 11).contains(month)) {
            day = 1 + RANDOM.nextInt(30);
        } else {
            // Ignore leap years to match PHP behavior
            day = 1 + RANDOM.nextInt(28);
        }

        LocalDate date = LocalDate.of(year, month, day);
        this.birthDate = date.format(DateTimeFormatter.ISO_DATE);
    }

    private void setFullNameAndGender() {
        try {
            byte[] jsonBytes = Files.readAllBytes(Path.of(FILE_PERSON_NAMES));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonBytes);
            JsonNode persons = root.get("persons");
            int idx = RANDOM.nextInt(persons.size());
            JsonNode person = persons.get(idx);

            this.firstName = person.get("firstName").asText();
            this.lastName = person.get("lastName").asText();
            this.gender = person.get("gender").asText();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read person names JSON", e);
        }
    }

    private void setAddress() {
        address.put("street", getRandomText(40, true));

        StringBuilder number = new StringBuilder();
        number.append(RANDOM.nextInt(999) + 1);
        if (RANDOM.nextInt(10) < 2) { // ~20%
            number.append(getRandomText(1, false).toUpperCase());
        }
        address.put("number", number.toString());

        if (RANDOM.nextInt(10) < 3) { // ~30%
            address.put("floor", "st");
        } else {
            address.put("floor", RANDOM.nextInt(99) + 1);
        }

        int doorType = RANDOM.nextInt(20) + 1;
        String door;
        if (doorType < 8) {
            door = "th";
        } else if (doorType < 15) {
            door = "tv";
        } else if (doorType < 17) {
            door = "mf";
        } else if (doorType < 19) {
            door = String.valueOf(RANDOM.nextInt(50) + 1);
        } else {
            List<String> lowerCaseLetters = Arrays.asList(
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                    "k", "l", "m", "n", "o", "p", "q", "r", "s",
                    "t", "u", "v", "w", "x", "y", "z", "ø", "æ", "å"
            );
            StringBuilder temp = new StringBuilder();
            temp.append(lowerCaseLetters.get(RANDOM.nextInt(lowerCaseLetters.size())));
            if (doorType == 20) {
                temp.append("-");
            }
            temp.append(RANDOM.nextInt(999) + 1);
            door = temp.toString();
        }
        address.put("door", door);

        Town town = new Town();
        Map<String, String> townData = town.getRandomTown();
        address.put("postal_code", townData.get("postal_code"));
        address.put("town_name", townData.get("town_name"));
    }

    private static String getRandomText(int length, boolean includeDanishCharacters) {
        List<String> valid = new ArrayList<>(Arrays.asList(
                " ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
        ));
        if (includeDanishCharacters) {
            valid.addAll(Arrays.asList("æ", "ø", "å", "Æ", "Ø", "Å"));
        }

        StringBuilder text = new StringBuilder();
        text.append(valid.get(1 + RANDOM.nextInt(valid.size() - 1)));
        for (int i = 1; i < length; i++) {
            text.append(valid.get(RANDOM.nextInt(valid.size())));
        }
        return text.toString();
    }

    private void setPhone() {
        String prefix = PHONE_PREFIXES.get(RANDOM.nextInt(PHONE_PREFIXES.size()));
        StringBuilder phoneBuilder = new StringBuilder(prefix);
        int remaining = 8 - prefix.length();
        for (int i = 0; i < remaining; i++) {
            phoneBuilder.append(getRandomDigit());
        }
        this.phone = phoneBuilder.toString();
    }

    public String getCpr() {
        return cpr;
    }

    public Map<String, Object> getFullNameAndGender() {
        Map<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("gender", gender);
        return result;
    }

    public Map<String, Object> getFullNameGenderAndBirthDate() {
        Map<String, Object> result = getFullNameAndGender();
        result.put("birthDate", birthDate);
        return result;
    }

    public Map<String, Object> getCprFullNameAndGender() {
        Map<String, Object> result = getFullNameAndGender();
        result.put("CPR", cpr);
        return result;
    }

    public Map<String, Object> getCprFullNameGenderAndBirthDate() {
        Map<String, Object> result = getCprFullNameAndGender();
        result.put("birthDate", birthDate);
        return result;
    }

    public Map<String, Object> getAddress() {
        Map<String, Object> result = new HashMap<>();
        result.put("address", address);
        return result;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public Map<String, Object> getFakePerson() {
        Map<String, Object> result = new HashMap<>();
        result.put("CPR", cpr);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("gender", gender);
        result.put("birthDate", birthDate);
        result.put("address", address);
        result.put("phoneNumber", phone);
        return result;
    }

    public List<Map<String, Object>> getFakePersons(int amount) {
        if (amount < MIN_BULK_PERSONS) amount = MIN_BULK_PERSONS;
        if (amount > MAX_BULK_PERSONS) amount = MAX_BULK_PERSONS;

        List<Map<String, Object>> bulk = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            FakeInfo info = new FakeInfo();
            bulk.add(info.getFakePerson());
        }
        return bulk;
    }

    private static String getRandomDigit() {
        return String.valueOf(RANDOM.nextInt(10));
    }
}