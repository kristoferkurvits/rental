package ee.kristofer.rental.service;

import ee.kristofer.rental.model.User;
import ee.kristofer.rental.model.UserRegistrationResponse;

public interface RegistrationService {
    UserRegistrationResponse register(User user);
}
