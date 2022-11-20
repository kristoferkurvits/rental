package ee.kristofer.rental.service;

import ee.kristofer.rental.exception.AuthorizationException;
import ee.kristofer.rental.exception.NotAcceptableException;
import ee.kristofer.rental.exception.NotFoundException;
import ee.kristofer.rental.exception.RentalException;
import ee.kristofer.rental.model.Coordinates;
import ee.kristofer.rental.model.EndRentRequest;
import ee.kristofer.rental.model.Reservation;
import ee.kristofer.rental.model.StartRentRequest;
import ee.kristofer.rental.model.database.ReservationDatabaseObject;
import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.model.database.VehicleDatabaseObject;
import ee.kristofer.rental.repository.ReservationRepository;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.repository.VehicleRepository;
import ee.kristofer.rental.serviceImpl.PricingServiceImpl;
import ee.kristofer.rental.serviceImpl.RentServiceImpl;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static ee.kristofer.rental.constants.Constants.USER_ID_PARAM;
import static ee.kristofer.rental.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RentServiceTests {

    private static final Instant START_TIME = Instant.parse("2022-12-03T10:00:00.00Z");
    private static final Instant END_TIME = Instant.parse("2022-12-03T10:12:11.00Z");

    @Mock
    private UserRepository userRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private PricingServiceImpl pricingService;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    RentServiceImpl rentService;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startRentSuccessful() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITHOUT_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleStartRent(false));
        when(userRepository.findById(any(String.class))).thenReturn(createUserStartRent(null));

        var response = rentService.startRent(createStartRentRequest(VEHICLE_ID,USER_ID_WITHOUT_VEHICLE));
        assertNotNull(response.getReservationId());
        assertNotNull(response.getStart());
    }

    @Test
    void startRentWrongUser() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITHOUT_VEHICLE);
        assertThrows(AuthorizationException.class,
                () -> rentService.startRent(createStartRentRequest(VEHICLE_ID, UUID.randomUUID().toString())));
    }

    @Test
    void startRentInvalidVehicle() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITHOUT_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> rentService.startRent(createStartRentRequest(UUID.randomUUID().toString(), USER_ID_WITHOUT_VEHICLE)));
    }

    @Test
    void startRentVehicleInUse() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITHOUT_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleStartRent(true));
        assertThrows(NotAcceptableException.class,
                () -> rentService.startRent(createStartRentRequest(VEHICLE_ID, USER_ID_WITHOUT_VEHICLE)));
    }

    @Test
    void startRentUserAlreadyHasOngoingReservation() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITHOUT_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleStartRent(false));
        when(userRepository.findById(any(String.class))).thenReturn(createUserStartRent(new ReservationDatabaseObject()));
        assertThrows(NotAcceptableException.class,
                () -> rentService.startRent(createStartRentRequest(VEHICLE_ID, USER_ID_WITHOUT_VEHICLE)));
    }

    private StartRentRequest createStartRentRequest(String vehicleId, String userId) {
        return new StartRentRequest()
                .setVehicleId(vehicleId)
                .setUserId(userId);
    }

    private Optional<UserDatabaseObject> createUserStartRent(ReservationDatabaseObject reservation) {
        return Optional.of(new UserDatabaseObject()
                .setId(USER_ID_WITHOUT_VEHICLE)
                .setOngoingReservation(reservation));
    }

    private Optional<VehicleDatabaseObject> createVehicleStartRent(boolean inUse) {
        return Optional.of(new VehicleDatabaseObject()
                .setInUse(inUse)
                .setId(VEHICLE_ID)
                .setCoordinates(new Coordinates())
                .setStateOfCharge(100));
    }

    //-----------------------------------------------------------------------------------------------------------------

    @Test
    void endRentSuccessful() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleEndRent(true, USER_ID_WITH_VEHICLE));
        when(userRepository.findById(any(String.class))).thenReturn(createUserEndRent(createReservation(), USER_ID_WITH_VEHICLE));

        var response = rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITH_VEHICLE));

        assertEquals(pricingService.calculatePrice(START_TIME, END_TIME), response.getCost());
        assertNotNull(response.getEndTime());
        assertEquals(RESERVATION_ID, response.getReservationId());
    }

    @Test
    void endRentWrongUser() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        assertThrows(AuthorizationException.class,
                () -> rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITHOUT_VEHICLE)));
    }

    @Test
    void endRentInvalidVehicle() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITH_VEHICLE)));
    }

    @Test
    void endRentVehicleAlreadyStopped() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleEndRent(false, USER_ID_WITH_VEHICLE));
        assertThrows(NotAcceptableException.class,
                () -> rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITH_VEHICLE)));
    }

    @Test
    void endRentUserNotFound() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleEndRent(true, USER_ID_WITH_VEHICLE));
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITH_VEHICLE)));
    }

    @Test
    void endRentUserAndVehicleDoNotMatch() {
        ThreadContext.put(USER_ID_PARAM, USER_ID_WITH_VEHICLE);
        when(vehicleRepository.findById(any(String.class))).thenReturn(createVehicleEndRent(true, USER_ID_WITHOUT_VEHICLE));
        when(userRepository.findById(any(String.class))).thenReturn(createUserEndRent(new ReservationDatabaseObject(), USER_ID_WITH_VEHICLE));
        assertThrows(NotAcceptableException.class,
                () -> rentService.endRent(createEndRentRequest(VEHICLE_ID, USER_ID_WITH_VEHICLE)));
    }

    private EndRentRequest createEndRentRequest(String vehicleId, String userId) {
        return new EndRentRequest()
                .setVehicleId(vehicleId)
                .setUserId(userId);
    }

    private Optional<UserDatabaseObject> createUserEndRent(ReservationDatabaseObject reservation, String userId) {
        return Optional.of(new UserDatabaseObject()
                .setId(userId)
                .setOngoingReservation(reservation));
    }

    private Optional<VehicleDatabaseObject> createVehicleEndRent(boolean inUse, String userId) {
        return Optional.of(new VehicleDatabaseObject()
                .setInUse(inUse)
                .setId(VEHICLE_ID)
                .setCoordinates(new Coordinates())
                .setStateOfCharge(100)
                .setUserId(userId));
    }

    private ReservationDatabaseObject createReservation() {
        return new ReservationDatabaseObject()
                .setId(RESERVATION_ID)
                .setStart(START_TIME)
                .setEnd(END_TIME);
    }

}
