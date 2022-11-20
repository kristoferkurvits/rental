package ee.kristofer.rental.service;

import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.User;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.serviceImpl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static ee.kristofer.rental.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RegistrationServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RegistrationServiceImpl registrationService;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void successfulRegistration() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(null);

        var response = registrationService.register(createUser());
        assertNotNull(response.getUserId());
    }

    @Test
    void userAlreadyRegistered() {
        when(userRepository.findByEmail(any(String.class))).thenReturn(new UserDatabaseObject());
        assertThrows(UnprocessableEntityException.class, () -> registrationService.register(createUser()));
    }

    private User createUser() {
        return new User()
                .setEmail(EMAIL)
                .setPassword(PASSWORD)
                .setName(NAME);
    }

}
