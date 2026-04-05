package com.group_nine.mandatory_one.providers;

import com.group_nine.mandatory_one.model.Town;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTownProvider implements TownProvider {

  private final String dbUrl;
  private final String dbUser;
  private final String dbPassword;
  private final Random random;

  public DatabaseTownProvider(
      Random random,
      @Value(
              "${spring.datasource.url:jdbc:mysql://localhost:3307/fakepersonadb"
                  + "?useUnicode=true&characterEncoding=utf8}")
          String dbUrl,
      @Value("${spring.datasource.username:root}") String dbUser,
      @Value("${spring.datasource.password:123456}") String dbPassword) {
    this.random = Objects.requireNonNull(random, "random must not be null");
    this.dbUrl = Objects.requireNonNull(dbUrl, "dbUrl must not be null");
    this.dbUser = Objects.requireNonNull(dbUser, "dbUser must not be null");
    this.dbPassword = Objects.requireNonNull(dbPassword, "dbPassword must not be null");
  }

  @Override
  public Town getRandomTown() {
    int townCount = countTowns();
    if (townCount == 0) {
      throw new IllegalStateException("No towns available in database");
    }

    int randomIndex = random.nextInt(townCount);

    String sql =
        "SELECT cPostalCode AS postal_code, cTownName AS town_name "
            + "FROM postal_code LIMIT ?, 1";

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, randomIndex);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Town(rs.getString("postal_code"), rs.getString("town_name"));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching random town", e);
    }

    throw new RuntimeException("No town found for index " + randomIndex);
  }

  private int countTowns() {
    String sql = "SELECT COUNT(*) AS total FROM postal_code";
    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      if (rs.next()) {
        return rs.getInt("total");
      }
      return 0;
    } catch (SQLException e) {
      throw new RuntimeException("Error counting towns", e);
    }
  }
}
