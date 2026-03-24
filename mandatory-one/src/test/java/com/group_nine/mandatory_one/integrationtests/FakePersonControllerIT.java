package com.group_nine.mandatory_one.integrationtests;

import com.group_nine.mandatory_one.FakePersonController;
import com.group_nine.mandatory_one.FakePersonService;
import com.group_nine.mandatory_one.model.Address;
import com.group_nine.mandatory_one.model.FakePerson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FakePersonControllerIT {

    @Autowired
    private FakePersonController controller;

    @Test
    @DisplayName("GET /api/cpr returns CPR JSON")
    void getCpr_returnsJson() {
        Map<String, String> response = controller.getCpr();
        
        assertNotNull(response.get("CPR"));
        assertEquals(10, response.get("CPR").length());
    }

    @Test
    @DisplayName("GET /api/person?n=5 returns list of 5 persons")
    void getPerson_bulk_returnsList() {
        ResponseEntity<?> response = controller.getPerson(5);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> body = (List<?>) response.getBody();
        assertNotNull(body);
        assertEquals(5, body.size());
    }

    @Test
    @DisplayName("GET /api/person?n=0 returns 400 Bad Request")
    void getPerson_invalidN_returnsBadRequest() {
        ResponseEntity<?> response = controller.getPerson(0);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Incorrect GET parameter value", body.get("error"));
    }

    @Test
    @DisplayName("GET /api/address returns a valid address")
    void getAddress_returnsValidAddress() {
        Map<String, Address> response = controller.getAddress();
        assertNotNull(response.get("address"));
        assertNotNull(response.get("address").townName());
    }
}
