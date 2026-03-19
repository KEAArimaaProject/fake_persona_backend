package com.group_nine.mandatory_one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Town {

    // Must match application.properties (Docker: port 3307, db fakepersonadb)
    private static final String DB_URL = "jdbc:mysql://localhost:3307/fakepersonadb?useUnicode=true&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    private static int townCount = 0;

    public Town() {
        ensureTownCount();
    }

    private void ensureTownCount() {
        if (townCount != 0) return;
        String sql = "SELECT COUNT(*) AS total FROM postal_code";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                townCount = rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting towns", e);
        }
    }

    public Map<String, String> getRandomTown() {
        if (townCount == 0) {
            throw new IllegalStateException("No towns available in database");
        }
        int randomIndex = (int) (Math.random() * townCount);

        String sql = "SELECT cPostalCode AS postal_code, cTownName AS town_name " +
                     "FROM postal_code LIMIT ?, 1";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, randomIndex);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> result = new HashMap<>();
                    result.put("postal_code", rs.getString("postal_code"));
                    result.put("town_name", rs.getString("town_name"));
                    return result;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching random town", e);
        }
        throw new RuntimeException("No town found for index " + randomIndex);
    }
}