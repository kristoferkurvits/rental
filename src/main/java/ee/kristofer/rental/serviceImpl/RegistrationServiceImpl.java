package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.constants.RestErrorType;
import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.Auth;
import ee.kristofer.rental.model.RentalDatabaseObject;
import ee.kristofer.rental.model.User;
import ee.kristofer.rental.model.UserRegistrationResponse;
import ee.kristofer.rental.repository.AuthRepository;
import ee.kristofer.rental.repository.RentalRepository;
import ee.kristofer.rental.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private static final String ALREADY_REGISTERED_MESSAGE = "This user has already been registered";

    private final RentalRepository rentalRepository;
    private final AuthRepository authRepository;

    @Override
    public UserRegistrationResponse register(User user) {
        //TODO validate

        if (userAlreadyExists(user.getEmail())) {
            throw new UnprocessableEntityException(
                    RestErrorType.USER_ALREADY_REGISTERED,
                    ALREADY_REGISTERED_MESSAGE);
        }
        var rentalDbEntity = new RentalDatabaseObject();
        var userId = UUID.randomUUID().toString();
        rentalDbEntity.setId(userId);
        rentalDbEntity.setEmail(user.getEmail());
        rentalDbEntity.setName(user.getName());
        rentalDbEntity.setPassword(user.getPassword());
        rentalRepository.insert(rentalDbEntity);
        log.debug("Saved: {}", rentalDbEntity);
        var auth = new Auth();
        auth.setUserId(userId);
        auth.setApiKey(userId);
        authRepository.save(auth);
        authRepository.findById(userId);
        var response = new UserRegistrationResponse();
        response.setUserId(userId);

        return response;
    }

    private boolean userAlreadyExists(String email) {
        return Objects.nonNull(rentalRepository.findByEmail(email));
    }
}
