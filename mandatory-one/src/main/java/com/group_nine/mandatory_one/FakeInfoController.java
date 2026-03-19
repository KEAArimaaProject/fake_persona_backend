package com.group_nine.mandatory_one;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = { "http://localhost:5500" })
@RequestMapping("/api")
public class FakeInfoController {

    @GetMapping("/cpr")
    public Map<String, String> getCpr() {
        FakeInfo fakeInfo = new FakeInfo();
        return Map.of("CPR", fakeInfo.getCpr());
    }

    @GetMapping("/name-gender")
    public Map<String, Object> getNameAndGender() {
        FakeInfo fakeInfo = new FakeInfo();
        return fakeInfo.getFullNameAndGender();
    }

    @GetMapping("/name-gender-dob")
    public Map<String, Object> getNameGenderDob() {
        FakeInfo fakeInfo = new FakeInfo();
        return fakeInfo.getFullNameGenderAndBirthDate();
    }

    @GetMapping("/cpr-name-gender")
    public Map<String, Object> getCprNameGender() {
        FakeInfo fakeInfo = new FakeInfo();
        return fakeInfo.getCprFullNameAndGender();
    }

    @GetMapping("/cpr-name-gender-dob")
    public Map<String, Object> getCprNameGenderDob() {
        FakeInfo fakeInfo = new FakeInfo();
        return fakeInfo.getCprFullNameGenderAndBirthDate();
    }

    @GetMapping("/address")
    public Map<String, Object> getAddress() {
        FakeInfo fakeInfo = new FakeInfo();
        return fakeInfo.getAddress();
    }

    @GetMapping("/phone")
    public Map<String, String> getPhone() {
        FakeInfo fakeInfo = new FakeInfo();
        return Map.of("phoneNumber", fakeInfo.getPhoneNumber());
    }

    @GetMapping("/person")
    public ResponseEntity<?> getPerson(@RequestParam(name = "n", required = false, defaultValue = "1") int n) {
        n = Math.abs(n);
        FakeInfo fakeInfo = new FakeInfo();

        if (n == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Incorrect GET parameter value"));
        } else if (n == 1) {
            return ResponseEntity.ok(fakeInfo.getFakePerson());
        } else if (n > 1 && n <= 100) {
            List<Map<String, Object>> persons = fakeInfo.getFakePersons(n);
            return ResponseEntity.ok(persons);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Incorrect GET parameter value"));
        }
    }
}

