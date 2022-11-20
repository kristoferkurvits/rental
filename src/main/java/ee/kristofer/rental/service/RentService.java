package ee.kristofer.rental.service;

import ee.kristofer.rental.model.EndRentResponse;
import ee.kristofer.rental.model.StartRentRequest;
import ee.kristofer.rental.model.StartRentResponse;
import ee.kristofer.rental.model.EndRentRequest;

public interface RentService {
    StartRentResponse startRent(StartRentRequest startRentRequest);
    EndRentResponse endRent(EndRentRequest endRentRequest);
}
