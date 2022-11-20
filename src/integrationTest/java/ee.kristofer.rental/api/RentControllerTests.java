package ee.kristofer.rental.api;

import ee.kristofer.rental.constants.EntityType;
import ee.kristofer.rental.handler.AuthFilter;
import ee.kristofer.rental.model.*;
import ee.kristofer.rental.model.database.ReservationDatabaseObject;
import ee.kristofer.rental.repository.ReservationRepository;
import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.repository.VehicleRepository;
import ee.kristofer.rental.util.MapperUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.Objects;

import static ee.kristofer.rental.constants.Constants.*;
import static ee.kristofer.rental.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RentControllerTests extends IntegrationTestBase {
    private static final String RENT_PATH = "/rent";
    private final MapperUtil mapperUtil = MapperUtil.getInstance();

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    AuthFilter authFilter;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilter(authFilter)
                .build();
        ReflectionTestUtils.setField(authFilter, "contextPath", contextPath);
    }

    @Test
    void startRent() throws Exception {
        var mvcresult = this.mockMvc.perform(post(contextPath + RENT_PATH + "/start")
                .contextPath(contextPath)
                .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION, APIKEY_FOR_USER_WITHOUT_VEHICLE)
                .content(mapperUtil.objectToJson(createStartRentRequest())))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        var response = mapperUtil.jsonToObject(mvcresult.getResponse().getContentAsString(), StartRentResponse.class);
        assertTrue(Objects.nonNull(response.getReservationId()));
        assertTrue(response.getStart().isBefore(Instant.now()));
        var startCoordinates = new Coordinates()
                .setLatitude(3.2)
                .setLongitude(5.44);
        reservationAssertions(
                USER_ID_WITHOUT_VEHICLE,
                VEHICLE_ID,
                startCoordinates,
                false
        );
        userAssertions(USER_ID_WITHOUT_VEHICLE, false);
        vehicleAssertions(VEHICLE_ID, false, startCoordinates);
    }

    @Test
    void endRent() throws Exception {
        var mvcresult = this.mockMvc.perform(post(contextPath + RENT_PATH + "/end")
                .contextPath(contextPath)
                .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION, APIKEY_FOR_USER_WITH_VEHICLE2)
                .content(mapperUtil.objectToJson(createEndRentRequest())))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();

        var response = mapperUtil.jsonToObject(mvcresult.getResponse().getContentAsString(), EndRentResponse.class);
        assertEquals(ONGOING_RESERVATION_ID_2, response.getReservationId());
        assertTrue(response.getEndTime().isBefore(Instant.now()));
        var startCoordinates = new Coordinates()
                .setLatitude(31.2)
                .setLongitude(51.44);
        reservationAssertions(
                USER_ID_WITH_VEHICLE_2,
                VEHICLE_ID_2,
                startCoordinates,
                true
        );
        userAssertions(USER_ID_WITH_VEHICLE_2, true);
        vehicleAssertions(VEHICLE_ID_2, true, startCoordinates);
    }

    private void vehicleAssertions(
            String vehicleId, boolean endRent, Coordinates reservationStartCoordinates) {
        var vehicle = vehicleRepository.findById(vehicleId).get();
        assertEquals(EntityType.VEHICLE, vehicle.getType());
        if (endRent) {
            assertFalse(vehicle.isInUse());
            assertNull(vehicle.getUserId());
            //Vehicle coordinates should change in time but they dont right now so start and end coordinates stay the same
            assertEquals(reservationStartCoordinates, vehicle.getCoordinates());
            return;
        }
        assertEquals(USER_ID_WITHOUT_VEHICLE, vehicle.getUserId());
        assertTrue(vehicle.isInUse());
        assertEquals(reservationStartCoordinates, vehicle.getCoordinates());
    }

    private void userAssertions(String userId, boolean endRent) {
        var user = userRepository.findById(userId).get();
        if (endRent) {
            assertNull(user.getOngoingReservation());
            return;
        }
        assertNotNull(user.getOngoingReservation());
    }

    private void reservationAssertions(String userId, String vehicleId, Coordinates startCoordinates, boolean endRent) {
        var reservation = reservationRepository.findByUserId(userId, PageRequest.of(0,1)).getContent();
        var activeReservation = reservation.stream()
                .filter(r -> userAndVehicleMatch(r, userId, vehicleId)).findFirst().get();

        assertEquals(userId, activeReservation.getUserId());
        assertEquals(vehicleId, activeReservation.getVehicleId());
        assertEquals(startCoordinates, activeReservation.getStartCoordinates());
        assertEquals(EntityType.RESERVATION, activeReservation.getType());
        assertNotNull(activeReservation.getStart());
        if (endRent) {
            assertNotNull(activeReservation.getEnd());
            //Vehicle coordinates should change in time but they dont right now so start and end coordinates stay the same
            assertEquals(startCoordinates, activeReservation.getEndCoordinates());
        }
    }

    private boolean userAndVehicleMatch(ReservationDatabaseObject r, String userId, String vehicleId) {
        return r.getUserId().equals(userId) && r.getVehicleId().equals(vehicleId);
    }

    private EndRentRequest createEndRentRequest() {
        return new EndRentRequest()
                .setUserId(USER_ID_WITH_VEHICLE_2)
                .setVehicleId(VEHICLE_ID_2);
    }

    private StartRentRequest createStartRentRequest() {
        return new StartRentRequest()
                .setUserId(USER_ID_WITHOUT_VEHICLE)
                .setVehicleId(VEHICLE_ID);
    }

}
