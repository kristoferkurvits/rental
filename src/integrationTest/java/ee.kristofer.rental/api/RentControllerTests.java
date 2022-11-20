package ee.kristofer.rental.api;

import ee.kristofer.rental.handler.AuthFilter;
import ee.kristofer.rental.model.EndRentRequest;
import ee.kristofer.rental.model.EndRentResponse;
import ee.kristofer.rental.model.StartRentRequest;
import ee.kristofer.rental.model.StartRentResponse;
import ee.kristofer.rental.util.MapperUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.Objects;

import static ee.kristofer.rental.constants.Constants.*;
import static ee.kristofer.rental.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
