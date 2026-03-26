package com.group_nine.mandatory_one.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.group_nine.mandatory_one.FakePersonController;
import com.group_nine.mandatory_one.model.Address;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FakePersonControllerIT {

  @Autowired private FakePersonController controller;

  @Test
  @DisplayName("GET /api/cpr returns CPR JSON")
  void getCprReturnsJson() {
    Map<String, String> response = controller.getCpr();

    assertNotNull(response.get("cpr"));
    assertEquals(10, response.get("cpr").length());
  }

  @Test
  @DisplayName("GET /api/person?n=5 returns list of 5 persons")
  void getPersonBulkReturnsList() {
    ResponseEntity<?> response = controller.getPerson(5);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<?> body = (List<?>) response.getBody();
    assertNotNull(body);
    assertEquals(5, body.size());
  }

  @Test
  @DisplayName("GET /api/person?n=0 returns 400 Bad Request")
  void getPersonInvalidNReturnsBadRequest() {
    ResponseEntity<?> response = controller.getPerson(0);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Map<?, ?> body = (Map<?, ?>) response.getBody();
    assertEquals("Incorrect GET parameter value", body.get("error"));
  }

  @Test
  @DisplayName("GET /api/address returns a valid address")
  void getAddressReturnsValidAddress() {
    Map<String, Address> response = controller.getAddress();
    assertNotNull(response.get("address"));
    assertNotNull(response.get("address").townName());
  }
}
