package ee.kristofer.rental.handler;

import ee.kristofer.rental.model.database.UserDatabaseObject;
import ee.kristofer.rental.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthFilterTest {

    private static final String CONTEXT_PATH = "/rental";
    private static final String USER_ID = "052b41c5-27a0-4aeb-aec4-33629aa07ad5";
    private static final String REGISTER_PATH = "/register";

    @Mock
    protected HttpServletRequest servletRequest;
    @Mock
    protected HttpServletResponse servletResponse;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthFilter authFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(authFilter, "contextPath", CONTEXT_PATH);
        when(servletRequest.getSession(any(Boolean.class))).thenReturn(new MockHttpSession());
    }

    @Test
    void successfulAuthentication() {
        var user = createUser();
        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn(new String(Base64.getEncoder().encode(USER_ID.getBytes(StandardCharsets.UTF_8))));
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+"/scooters");
        when(userRepository.findById(any(String.class))).thenReturn(Optional.of(user));

        assertTrue(authFilter.validRequest(servletRequest, servletResponse));
    }

    private UserDatabaseObject createUser() {
        var user = new UserDatabaseObject();
        user.setId(USER_ID);
        return user;
    }

    @Test
    void registrationPathAuthenticationWithoutApikey() {
        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn("");
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+REGISTER_PATH);
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertTrue(authFilter.validRequest(servletRequest, servletResponse));
    }

    @Test
    void unsuccessfulAuthentication() {
        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn("");
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+"/scooters");
        when(userRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertFalse(authFilter.validRequest(servletRequest, servletResponse));
    }

}
