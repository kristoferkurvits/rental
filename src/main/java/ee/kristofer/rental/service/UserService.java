package ee.kristofer.rental.service;

import ee.kristofer.rental.model.UserReservationsRequest;
import ee.kristofer.rental.model.UserReservationsResponse;

import java.util.Map;

public interface UserService {
    Map<String, Object> getReservations(UserReservationsRequest userReservationsRequest, int page, int size);
}
