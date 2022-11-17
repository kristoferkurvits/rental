package ee.kristofer.rental.controller;

import ee.kristofer.rental.model.UserRegistrationResponse;
import ee.kristofer.rental.model.User;
import ee.kristofer.rental.serviceImpl.RegistrationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationServiceImpl registrationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@Valid @RequestBody User user) {
        return ResponseEntity.ok(registrationService.register(user));
    }
}
