package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.exception.AuthorizationException;
import ee.kristofer.rental.exception.NotFoundException;
import ee.kristofer.rental.model.*;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.model.database.VehicleDatabaseObject;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.repository.VehicleRepository;
import ee.kristofer.rental.service.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static ee.kristofer.rental.constants.Constants.USER_ID_PARAM;

@Service
@RequiredArgsConstructor
@Log4j2
public class RentServiceImpl implements RentService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PricingServiceImpl pricingService;


    @Override
    public StartRentResponse startRent(StartRentRequest startRentRequest) {
        if (!validUser(startRentRequest.getUserId()))  {
            throw new AuthorizationException();
        }
        var optionalVehicle = vehicleRepository.findById(startRentRequest.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Invalid vehicle");
        }
        var optionalUser = userRepository.findById(startRentRequest.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid user");
        }

        var vehicle = optionalVehicle.get();
        vehicle.setUserId(startRentRequest.getUserId());
        vehicle.setInUse(true);

        var user = optionalUser.get();
        var reservation = createReservation(vehicle);

        user.setOngoingReservation(reservation);
        user.setVehicle(vehicle);
        user.setModifiedAt(Instant.now());

        vehicleRepository.save(vehicle);
        userRepository.save(user);
        return new StartRentResponse()
                .setReservationId(reservation.getId())
                .setStart(Instant.now());
    }

    @Override
    public EndRentResponse endRent(EndRentRequest endRentRequest) {
        if (!validUser(endRentRequest.getUserId()))  {
            throw new AuthorizationException();
        }
        var optionalVehicle = vehicleRepository.findById(endRentRequest.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Invalid vehicle");
        }
        var optionalUser = userRepository.findById(endRentRequest.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid user");
        }

        var vehicle = optionalVehicle.get();
        var user = optionalUser.get();
        var endTime = Instant.now();
        var ongoingReservation = user.getOngoingReservation();
        var cost = pricingService.calculatePrice(ongoingReservation.getStart(), endTime);
        //Bill user
        ongoingReservation
                .setCost(cost)
                .setEndCoordinates(vehicle.getCoordinates())
                .setEnd(endTime);

        resetUserReservation(user, ongoingReservation);
        resetVehicleReservation(vehicle);
        return new EndRentResponse()
                .setEndTime(endTime)
                .setCost(cost)
                .setReservationId(ongoingReservation.getId());
    }
    private void resetUserReservation(UserDatabaseObject user, Reservation ongoingReservation) {
        var previousReservations = user.getPreviousReservations();
        if (Objects.nonNull(previousReservations)) {
            user.getPreviousReservations().add(ongoingReservation);
        }
        user.setOngoingReservation(null);
        userRepository.save(user);
    }

    private void resetVehicleReservation(VehicleDatabaseObject vehicle) {
        vehicle.setUserId(null);
        vehicle.setModifiedAt(Instant.now());
        vehicle.setInUse(false);
        vehicleRepository.save(vehicle);
    }

    private boolean validUser(String userId) {
        return userId.equals(ThreadContext.get(USER_ID_PARAM));
    }


    private Reservation createReservation(VehicleDatabaseObject vehicle) {
        return new Reservation()
                .setId(UUID.randomUUID().toString())
                .setStartCoordinates(vehicle.getCoordinates());
    }
}
