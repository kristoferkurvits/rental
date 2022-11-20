package ee.kristofer.rental.api;

import ee.kristofer.rental.handler.AuthFilter;
import ee.kristofer.rental.model.UserRegistrationRequest;
import ee.kristofer.rental.model.UserRegistrationResponse;
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

import static ee.kristofer.rental.constants.Constants.CONTENT_TYPE;
import static ee.kristofer.rental.constants.Constants.CONTENT_TYPE_JSON;
import static ee.kristofer.rental.handler.AuthFilter.REGISTRATION_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
Integration tests should be split from unit tests so that integration tests are not run on every build
 */
public class RegistrationControllerTests extends IntegrationTestBase {

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
    void registerNewUser() throws Exception {
        var mvcResult = performPostRequest(createUser("heya@gmail.com"), status().isOk());
        var response = mapperUtil.jsonToObject(mvcResult.getResponse().getContentAsString(), UserRegistrationResponse.class);
        Assertions.assertTrue(Objects.nonNull(response.getUserId()));
    }

    @Test
    void registerSameUserTwice() throws Exception {
        var validEmail = "validemail@hotmail.com";
        performPostRequest(createUser(validEmail), status().isOk());
        performPostRequest(createUser(validEmail), status().isUnprocessableEntity());
    }

    @Test
    void invalidEmailOnRegistration() throws Exception {
        var user = createUser("Dumbledore@hot.ee");
        user.setEmail("");
        performPostRequest(user, status().isUnprocessableEntity());
    }

    private MvcResult performPostRequest(UserRegistrationRequest userRegistrationRequest, ResultMatcher resultMatcher) throws Exception {
        return this.mockMvc.perform(post(contextPath + REGISTRATION_PATH)
            .contextPath(contextPath)
            .header(CONTENT_TYPE, CONTENT_TYPE_JSON)
            .content(mapperUtil.objectToJson(userRegistrationRequest)))
        .andDo(print()).andExpect(resultMatcher)
        .andReturn();
    }

    private UserRegistrationRequest createUser(String email) {
        var user = new UserRegistrationRequest();
        user.setEmail(email);
        user.setName("name");
        user.setPassword("123456789");
        return user;
    }

}
