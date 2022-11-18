package ee.kristofer.rental.service;

import ee.kristofer.rental.model.VehicleResponse;
import ee.kristofer.rental.model.UpdateVehicleRequest;
import ee.kristofer.rental.model.Vehicle;

public interface VehicleService {
    VehicleResponse createVehicle(Vehicle vehicle);
    VehicleResponse updateVehicle(UpdateVehicleRequest vehicle);
}
