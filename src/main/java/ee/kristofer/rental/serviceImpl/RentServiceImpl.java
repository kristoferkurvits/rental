package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.exception.AuthorizationException;
import ee.kristofer.rental.exception.NotAcceptableException;
import ee.kristofer.rental.exception.NotFoundException;
import ee.kristofer.rental.exception.RentalException;
import ee.kristofer.rental.model.*;
import ee.kristofer.rental.model.database.ReservationDatabaseObject;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.model.database.VehicleDatabaseObject;
import ee.kristofer.rental.repository.ReservationRepository;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.repository.VehicleRepository;
import ee.kristofer.rental.service.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.IllegalFormatCodePointException;
import java.util.Objects;
import java.util.UUID;

import static ee.kristofer.rental.constants.Constants.USER_ID_PARAM;
import static ee.kristofer.rental.util.ValidationUtil.validUser;

@Service
@RequiredArgsConstructor
@Log4j2
public class RentServiceImpl implements RentService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final PricingServiceImpl pricingService;
    private final ReservationRepository reservationRepository;


    @Override
    public StartRentResponse startRent(StartRentRequest startRentRequest) {
        if (!validUser(startRentRequest.getUserId()))  {
            throw new AuthorizationException();
        }
        var optionalVehicle = vehicleRepository.findById(startRentRequest.getVehicleId());
        if (optionalVehicle.isEmpty()) {
            throw new NotFoundException("Invalid vehicle");
        }
        var vehicle = optionalVehicle.get();
        if (vehicle.isInUse()) {
            throw new NotAcceptableException("Vehicle already in use");
        }

        var optionalUser = userRepository.findById(startRentRequest.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid user");
        }

        var user = optionalUser.get();
        if (Objects.nonNull(user.getOngoingReservation())) {
            throw new NotAcceptableException("User has already ongoing reservation");
        }


        vehicle.setUserId(startRentRequest.getUserId());
        vehicle.setInUse(true);

        var reservation = createReservation(vehicle, user.getId());
        user.setOngoingReservation(reservation);
        user.setModifiedAt(Instant.now());

        vehicleRepository.save(vehicle);
        userRepository.save(user);
        reservationRepository.save(reservation);
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
        var vehicle = optionalVehicle.get();
        if (!vehicle.isInUse()) {
            throw new NotAcceptableException("Vehicle already stopped");
        }

        var optionalUser = userRepository.findById(endRentRequest.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Invalid user");
        }
        var user = optionalUser.get();
        if (!userAndVehicleMatch(user.getId(), vehicle.getUserId())) {
            throw new NotAcceptableException("User can only end their reservation");
        }

        var endTime = Instant.now();
        var reservation = user.getOngoingReservation();
        var cost = pricingService.calculatePrice(reservation.getStart(), endTime);
        reservation
            .setCost(cost)
            .setEndCoordinates(vehicle.getCoordinates()) //Vehicle coordinates should change in time
            .setEnd(endTime)
            .setActive(false);
        reservationRepository.save(reservation);

        resetUserReservation(user);
        resetVehicleReservation(vehicle);
        return new EndRentResponse()
                .setEndTime(endTime)
                .setCost(cost)
                .setReservationId(reservation.getId());
    }

    private boolean userAndVehicleMatch(String userId, String userIdOnVehicle) {
        return userId.equals(userIdOnVehicle);
    }

    private void resetUserReservation(UserDatabaseObject user) {
        user.setOngoingReservation(null);
        userRepository.save(user);
    }

    private void resetVehicleReservation(VehicleDatabaseObject vehicle) {
        vehicle.setUserId(null);
        vehicle.setModifiedAt(Instant.now());
        vehicle.setInUse(false);
        vehicleRepository.save(vehicle);
    }

    private ReservationDatabaseObject createReservation(VehicleDatabaseObject vehicle, String userId) {
        return new ReservationDatabaseObject()
                .setUserId(userId)
                .setId(UUID.randomUUID().toString())
                .setStartCoordinates(vehicle.getCoordinates())
                .setVehicleId(vehicle.getId());
    }
}
