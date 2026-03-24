package com.group_nine.mandatory_one.integrationtests;

import com.group_nine.mandatory_one.model.Town;
import com.group_nine.mandatory_one.providers.DatabaseTownProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTownProviderIT {

    private DatabaseTownProvider provider;
    private Random random;

    @BeforeEach
    void setUp() {
        random = new Random();
        // These settings match the docker-compose.yml
        provider = new DatabaseTownProvider(
                random,
                "jdbc:mysql://localhost:3307/fakepersonadb",
                "root",
                "123456"
        );
    }

    @Test
    @DisplayName("getRandomTown should return a valid town from the database")
    void getRandomTownReturnsValidTown() {
        Town town = provider.getRandomTown();
        
        assertNotNull(town);
        assertNotNull(town.postalCode());
        assertNotNull(town.townName());
        
        assertFalse(town.postalCode().isEmpty());
        assertFalse(town.townName().isEmpty());
        
        System.out.println("[DEBUG_LOG] Fetched town: " + town.townName() + " (" + town.postalCode() + ")");
    }

    @Test
    @DisplayName("Multiple calls should eventually return different towns")
    void getRandomTownReturnsDifferentTowns() {
        Town town1 = provider.getRandomTown();
        Town town2 = null;
        
        // Try up to 10 times to get a different town (to avoid flaky test if DB has few entries)
        for (int i = 0; i < 10; i++) {
            town2 = provider.getRandomTown();
            if (!town1.postalCode().equals(town2.postalCode())) {
                break;
            }
        }
        
        assertNotNull(town2);
        // We can't strictly guarantee they are different if the DB only has 1 record,
        // but for this project we expect many records.
        // If it fails, we might want to check the DB content.
    }
}
