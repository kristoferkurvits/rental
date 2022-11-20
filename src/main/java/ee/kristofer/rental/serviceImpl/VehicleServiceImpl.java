package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.exception.NotFoundException;
import ee.kristofer.rental.model.Coordinates;
import ee.kristofer.rental.model.VehicleResponse;
import ee.kristofer.rental.model.UpdateVehicleRequest;
import ee.kristofer.rental.model.CreateVehicleRequest;
import ee.kristofer.rental.model.database.VehicleDatabaseObject;
import ee.kristofer.rental.repository.VehicleRepository;
import ee.kristofer.rental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public VehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest) {
        var vehicleId = UUID.randomUUID().toString();
        vehicleRepository.save(createVehicleEntity(createVehicleRequest, vehicleId));
        return new VehicleResponse()
                .setVehicleId(vehicleId);
    }

    @Override
    public VehicleResponse updateVehicle(UpdateVehicleRequest vehicle) {
        var optionalExistingVehicle = vehicleRepository.findById(vehicle.getId());
        if(optionalExistingVehicle.isEmpty()) {
            throw new NotFoundException("Vehicle not found");
        }
        var existingVehicle = optionalExistingVehicle.get();
        vehicleRepository.save(
                updateExistingVehicle(existingVehicle, vehicle)
                        .setModifiedAt(Instant.now())
        );
        return new VehicleResponse()
                .setVehicleId(existingVehicle.getId());
    }

    private VehicleDatabaseObject updateExistingVehicle(
            VehicleDatabaseObject existingVehicle, UpdateVehicleRequest updateVehicleRequest) {
        var newCoordinates = updateVehicleRequest.getCoordinates();
        Coordinates coordinates = null;
        if (Objects.nonNull(newCoordinates)) {
            coordinates = new Coordinates()
                    .setLatitude(Objects.nonNull(newCoordinates.getLatitude())
                            ? newCoordinates.getLatitude() : existingVehicle.getCoordinates().getLatitude())
                    .setLongitude(Objects.nonNull(newCoordinates.getLongitude())
                            ? newCoordinates.getLongitude() : existingVehicle.getCoordinates().getLongitude());
        }
        return existingVehicle
                .setStateOfCharge(Objects.nonNull(updateVehicleRequest.getStateOfCharge()) ? updateVehicleRequest.getStateOfCharge() : existingVehicle.getStateOfCharge())
                .setCoordinates(Objects.nonNull(coordinates) ? coordinates : existingVehicle.getCoordinates());
    }

    private VehicleDatabaseObject createVehicleEntity(CreateVehicleRequest createVehicleRequest, String vehicleId) {
        return new VehicleDatabaseObject()
                .setId(vehicleId)
                .setCoordinates(createVehicleRequest.getCoordinates())
                .setStateOfCharge(createVehicleRequest.getStateOfCharge());
    }
}
