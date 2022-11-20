package ee.kristofer.rental.service;

import ee.kristofer.rental.model.VehicleResponse;
import ee.kristofer.rental.model.UpdateVehicleRequest;
import ee.kristofer.rental.model.CreateVehicleRequest;

public interface VehicleService {
    VehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest);
    VehicleResponse updateVehicle(UpdateVehicleRequest vehicle);
}
