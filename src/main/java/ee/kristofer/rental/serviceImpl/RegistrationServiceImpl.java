package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.constants.RestErrorType;
import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.RentalDatabaseObject;
import ee.kristofer.rental.model.User;
import ee.kristofer.rental.model.UserRegistrationResponse;
import ee.kristofer.rental.repository.RentalRepository;
import ee.kristofer.rental.service.RegistrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private static final Logger log = LogManager.getLogger(RegistrationServiceImpl.class);
    private static final String ALREADY_REGISTERED_MESSAGE = "This user has already been registered";

    private final RentalRepository rentalRepository;

    public RegistrationServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public UserRegistrationResponse register(User user) {
        //TODO validate

        if (userAlreadyExists(user.getId())) {
            throw new UnprocessableEntityException(
                    RestErrorType.USER_ALREADY_REGISTERED,
                    ALREADY_REGISTERED_MESSAGE);
        }

        var rentalDbEntity = new RentalDatabaseObject();
        rentalDbEntity.setId(user.getId());
        rentalDbEntity.setEmail(user.getEmail());
        rentalDbEntity.setName(user.getName());
        rentalDbEntity.setPassword(user.getPassword());
        rentalRepository.insert(rentalDbEntity);
        log.debug("Saved: {}", rentalDbEntity);
        return new UserRegistrationResponse();
    }

    private boolean userAlreadyExists(String id) {
        return rentalRepository.findById(id).isPresent();
    }
}
