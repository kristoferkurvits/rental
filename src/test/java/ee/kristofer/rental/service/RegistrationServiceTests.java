package ee.kristofer.rental.service;

import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.UserRegistrationRequest;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.serviceImpl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static ee.kristofer.rental.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RegistrationServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    RegistrationServiceImpl registrationService;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void successfulRegistration() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);
        when(passwordEncoder.encode(any(String.class))).thenReturn("$2a$10$ZLhnHxdpHETcxmtEStgpI./Ri1mksgJ9iDP36FmfMdYyVg9g0b2dq");
        var response = registrationService.register(createUser());
        assertNotNull(response.getUserId());
    }

    @Test
    void userAlreadyRegistered() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(new UserDatabaseObject());
        assertThrows(UnprocessableEntityException.class, () -> registrationService.register(createUser()));
    }

    private UserRegistrationRequest createUser() {
        return new UserRegistrationRequest()
                .setEmail(EMAIL)
                .setPassword(PASSWORD)
                .setName(NAME);
    }

}
