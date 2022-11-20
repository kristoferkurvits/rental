package ee.kristofer.rental.service;

import ee.kristofer.rental.model.UserRegistrationRequest;
import ee.kristofer.rental.model.UserRegistrationResponse;

public interface RegistrationService {
    UserRegistrationResponse register(UserRegistrationRequest userRegistrationRequest);
}
