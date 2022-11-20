package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.constants.RestErrorType;
import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.model.User;
import ee.kristofer.rental.model.UserRegistrationResponse;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private static final String ALREADY_REGISTERED_MESSAGE = "This user has already been registered";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegistrationResponse register(User user) {
        if (userAlreadyExists(user.getEmail())) {
            throw new UnprocessableEntityException(
                    RestErrorType.USER_ALREADY_REGISTERED,
                    ALREADY_REGISTERED_MESSAGE);
        }
        var userId = UUID.randomUUID().toString();
        var userEntity = createUser(user, userId);
        userRepository.insert(userEntity);

        log.debug("Saved: {}", userEntity);

        var response = new UserRegistrationResponse();
        response.setUserId(userId);
        return response;
    }

    private UserDatabaseObject createUser(User user, String userId) {
        return new UserDatabaseObject()
            .setId(userId)
            .setEmail(user.getEmail())
            .setPassword(passwordEncoder.encode(user.getPassword()))
            .setName(user.getName());
    }

    private boolean userAlreadyExists(String email) {
        return Objects.nonNull(userRepository.findByEmail(email));
    }
}
