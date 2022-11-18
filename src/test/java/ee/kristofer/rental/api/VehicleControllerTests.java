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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import static ee.kristofer.rental.constants.Constants.*;
import static ee.kristofer.rental.handler.AuthFilter.REGISTRATION_PATH;
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
            .header(AUTHORIZATION, "MDIwMDAwMGQtY2I1OC00NTVlLWI4NDEtN2NmOGVjYTc1MjRk") // TODO Need to add test data to DB
            .content(mapperUtil.objectToJson(vehicle)))
            .andDo(print()).andExpect(resultMatcher)
            .andReturn();
    }

    private MvcResult performPutRequest(Vehicle vehicle, ResultMatcher resultMatcher) throws Exception {
        return this.mockMvc.perform(put(contextPath + VEHICLE_PATH)
            .contextPath(contextPath)
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .content(mapperUtil.objectToJson(vehicle)))
            .andDo(print()).andExpect(resultMatcher)
            .andReturn();
    }
}
