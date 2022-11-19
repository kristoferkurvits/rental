package ee.kristofer.rental.api;

import ee.kristofer.rental.handler.AuthFilter;
import ee.kristofer.rental.model.*;
import ee.kristofer.rental.util.MapperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.UUID;

import static ee.kristofer.rental.constants.Constants.*;
import static ee.kristofer.rental.constants.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VehicleControllerTests extends IntegrationTestBase {
    private static final String VEHICLE_PATH = "/vehicle";
    private final MapperUtil mapperUtil = MapperUtil.getInstance();

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    AuthFilter authFilter;

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
    void createVehicleSuccessfully() throws Exception {
        var mvcResult = performPostRequest(createVehicle(), status().isOk());
        var response = mapperUtil.jsonToObject(mvcResult.getResponse().getContentAsString(), VehicleResponse.class);
        Assertions.assertTrue(Objects.nonNull(response.getVehicleId()));
    }

    @Test
    void updateVehicle() throws Exception {
        var mvcResult = performPutRequest(createUpdateVehicleRequest(), status().isOk());
        var response = mapperUtil.jsonToObject(mvcResult.getResponse().getContentAsString(), VehicleResponse.class);
        Assertions.assertEquals(VEHICLE_ID, response.getVehicleId());
    }

    @Test
    void updateVehicleNotFound() throws Exception {
        performPutRequest(createUpdateVehicleRequest()
                .setId(UUID.randomUUID().toString()), status().isNotFound());
    }

    private UpdateVehicleRequest createUpdateVehicleRequest() {
        return new UpdateVehicleRequest()
                .setId(VEHICLE_ID)
                .setStateOfCharge(50)
                .setCoordinates(new Coordinates()
                        .setLongitude(5.44)
                        .setLatitude(3.20));
    }

    private Vehicle createVehicle() {
        return new Vehicle()
            .setStateOfCharge(100)
            .setCoordinates(
                new Coordinates()
                    .setLongitude(25.44)
                    .setLatitude(30.20)
            );
    }

    private MvcResult performPostRequest(Vehicle vehicle, ResultMatcher resultMatcher) throws Exception {
        return this.mockMvc.perform(post(contextPath + VEHICLE_PATH)
            .contextPath(contextPath)
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .header(AUTHORIZATION, APIKEY_FOR_USER_WITH_VEHICLE)
            .content(mapperUtil.objectToJson(vehicle)))
            .andDo(print()).andExpect(resultMatcher)
            .andReturn();
    }

    private MvcResult performPutRequest(UpdateVehicleRequest vehicle, ResultMatcher resultMatcher) throws Exception {
        return this.mockMvc.perform(put(contextPath + VEHICLE_PATH)
            .contextPath(contextPath)
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .header(AUTHORIZATION, APIKEY_FOR_USER_WITH_VEHICLE)
            .content(mapperUtil.objectToJson(vehicle)))
            .andDo(print()).andExpect(resultMatcher)
            .andReturn();
    }
}
