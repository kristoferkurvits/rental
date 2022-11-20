package ee.kristofer.rental.api;

import ee.kristofer.rental.handler.AuthFilter;
import ee.kristofer.rental.model.UserReservationsRequest;
import ee.kristofer.rental.util.MapperUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static ee.kristofer.rental.constants.Constants.*;
import static ee.kristofer.rental.constants.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTests extends IntegrationTestBase {

    private static final String USER_PATH = "/user";
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
    void getReservations() throws Exception {
        this.mockMvc.perform(get(contextPath + USER_PATH + "/reservations")
                .contextPath(contextPath)
                .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
                .header(AUTHORIZATION, APIKEY_FOR_USER_WITH_VEHICLE)
                .content(mapperUtil.objectToJson(createUserReservationsRequest())))
                .andDo(print()).andExpect(status().isOk())
                .andReturn();
    }

    private UserReservationsRequest createUserReservationsRequest() {
        return new UserReservationsRequest()
                .setUserId(USER_ID_WITH_VEHICLE);
    }
}
