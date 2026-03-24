package com.group_nine.mandatory_one;

import com.group_nine.mandatory_one.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = { "http://localhost:5500" })
@RequestMapping("/api")
public class FakePersonController {

    private final FakePersonService service;

    public FakePersonController(FakePersonService service) {
        this.service = service;
    }

    private static ResponseEntity<Map<String, String>> badRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Incorrect GET parameter value"));
    }

    @GetMapping("/cpr")
    public Map<String, String> getCpr() {
        return Map.of("cpr", service.generateCpr());
    }

    @GetMapping("/name-gender")
    public PersonName getNameAndGender() {
        return service.generateNameAndGender();
    }

    @GetMapping("/name-gender-dob")
    public NameGenderDob getNameGenderDob() {
        return service.generateNameGenderAndBirthDate();
    }

    @GetMapping("/cpr-name-gender")
    public CprNameGender getCprNameGender() {
        return service.generateCprNameAndGender();
    }

    @GetMapping("/cpr-name-gender-dob")
    public CprNameGenderDob getCprNameGenderDob() {
        return service.generateCprNameGenderAndBirthDate();
    }

    @GetMapping("/address")
    public Map<String, Address> getAddress() {
        return Map.of("address", service.generateAddress());
    }

    @GetMapping("/phone")
    public Map<String, String> getPhone() {
        return Map.of("phoneNumber", service.generatePhone());
    }

    @GetMapping("/person")
    public ResponseEntity<?> getPerson(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        if (n < 1 || n > FakePersonService.MAX_BULK_PERSONS) {
            return badRequest();
        }

        if (n == 1) {
            return ResponseEntity.ok(service.generateFakePerson());
        }

        return ResponseEntity.ok(service.generateFakePersons(n));
    }
}
