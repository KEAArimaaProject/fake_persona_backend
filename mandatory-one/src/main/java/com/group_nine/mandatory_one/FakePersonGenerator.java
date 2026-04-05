package com.group_nine.mandatory_one;

import com.group_nine.mandatory_one.model.Address;
import com.group_nine.mandatory_one.model.Town;
import com.group_nine.mandatory_one.providers.TownProvider;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class FakePersonGenerator {

  private final Clock clock;

  public FakePersonGenerator() {
    this(Clock.systemDefaultZone());
  }

  public FakePersonGenerator(Clock clock) {
    this.clock = clock;
  }

  private static final List<String> PHONE_PREFIXES =
      List.of(
          "2", "30", "31", "40", "41", "42", "50", "51", "52", "53", "60", "61", "71", "81", "91",
          "92", "93", "342", "344", "345", "346", "347", "348", "349", "356", "357", "359", "362",
          "365", "366", "389", "398", "431", "441", "462", "466", "468", "472", "474", "476", "478",
          "485", "486", "488", "489", "493", "494", "495", "496", "498", "499", "542", "543", "545",
          "551", "552", "556", "571", "572", "573", "574", "577", "579", "584", "586", "587", "589",
          "597", "598", "627", "629", "641", "649", "658", "662", "663", "664", "665", "667", "692",
          "693", "694", "697", "771", "772", "782", "783", "785", "786", "788", "789", "826", "827",
          "829");

  private static final List<String> LOWER_CASE_LETTERS =
      List.of(
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
          "s", "t", "u", "v", "w", "x", "y", "z", "ø", "æ", "å");

  private static final List<String> RANDOM_TEXT_CHARS = buildRandomTextChars();

  public String generateBirthDate(Random random) {
    LocalDate startDate = LocalDate.of(1900, 1, 1);
    long startEpochDay = startDate.toEpochDay();
    long endEpochDay = LocalDate.now(clock).toEpochDay();
    long randomDay = startEpochDay + random.nextLong(endEpochDay - startEpochDay + 1);

    return LocalDate.ofEpochDay(randomDay).format(DateTimeFormatter.ISO_DATE);
  }

  public String generateCpr(Random random, String birthDate, String gender) {
    LocalDate date = LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE);

    int lastDigit = random.nextInt(10);
    boolean isMale = "male".equalsIgnoreCase(gender);

    // If male, last digit must be odd. If female, last digit must be even.
    if (isMale != (lastDigit % 2 != 0)) {
      lastDigit = (lastDigit + 1) % 10;
    }

    return String.format(
        "%02d%02d%02d%04d",
        date.getDayOfMonth(),
        date.getMonthValue(),
        date.getYear() % 100,
        random.nextInt(900) * 10 + lastDigit);
  }

  public String generatePhone(Random random) {
    String prefix = PHONE_PREFIXES.get(random.nextInt(PHONE_PREFIXES.size()));
    StringBuilder phoneBuilder = new StringBuilder(prefix);

    while (phoneBuilder.length() < 8) {
      phoneBuilder.append(getRandomDigit(random));
    }
    return phoneBuilder.toString();
  }

  public Address generateAddress(Random random, TownProvider townProvider) {
    Town townData = townProvider.getRandomTown();
    return new Address(
        getRandomText(random, 40, true),
        generateHouseNumber(random),
        generateFloor(random),
        generateDoor(random),
        townData.postalCode(),
        townData.townName());
  }

  private String generateHouseNumber(Random random) {
    StringBuilder number = new StringBuilder(String.valueOf(random.nextInt(999) + 1));
    if (random.nextInt(10) < 2) {
      number.append(getRandomText(random, 1, false).toUpperCase());
    }
    return number.toString();
  }

  private String generateFloor(Random random) {
    return random.nextInt(10) < 3 ? "st" : String.valueOf(random.nextInt(99) + 1);
  }

  private String generateDoor(Random random) {
    int doorType = random.nextInt(20) + 1;

    if (doorType < 8) return "th";
    if (doorType < 15) return "tv";
    if (doorType < 17) return "mf";
    if (doorType < 19) return String.valueOf(random.nextInt(50) + 1);

    StringBuilder door = new StringBuilder();
    door.append(LOWER_CASE_LETTERS.get(random.nextInt(LOWER_CASE_LETTERS.size())));
    if (doorType == 20) {
      door.append("-");
    }
    door.append(random.nextInt(999) + 1);
    return door.toString();
  }

  private String getRandomText(Random random, int length, boolean includeDanishCharacters) {
    List<String> valid = new ArrayList<>(RANDOM_TEXT_CHARS);
    if (!includeDanishCharacters) {
      valid.removeIf(ch -> "æøåÆØÅ".contains(ch));
    }

    StringBuilder text = new StringBuilder();
    text.append(valid.get(1 + random.nextInt(valid.size() - 1)));
    for (int i = 1; i < length; i++) {
      text.append(valid.get(random.nextInt(valid.size())));
    }
    return text.toString();
  }

  private static List<String> buildRandomTextChars() {
    List<String> valid =
        new ArrayList<>(
            List.of(
                " ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                "Y", "Z"));
    valid.addAll(List.of("æ", "ø", "å", "Æ", "Ø", "Å"));
    return List.copyOf(valid);
  }

  private String getRandomDigit(Random random) {
    return String.valueOf(random.nextInt(10));
  }
}
