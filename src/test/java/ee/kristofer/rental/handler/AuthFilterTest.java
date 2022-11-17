package ee.kristofer.rental.handler;

import ee.kristofer.rental.model.Auth;
import ee.kristofer.rental.repository.AuthRepository;
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
    private AuthRepository authRepository;

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
        var auth = new Auth();
        auth.setApiKey(new String(Base64.getEncoder().encode(USER_ID.getBytes(StandardCharsets.UTF_8))));
        auth.setUserId(USER_ID);

        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn(auth.getApiKey());
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+"/scooters");
        when(authRepository.findById(any(String.class))).thenReturn(auth);

        assertTrue(authFilter.validRequest(servletRequest, servletResponse));
    }

    @Test
    void registrationPathAuthenticationWithoutApikey() {
        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn("");
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+REGISTER_PATH);
        when(authRepository.findById(any(String.class))).thenReturn(null);

        assertTrue(authFilter.validRequest(servletRequest, servletResponse));
    }

    @Test
    void unsuccessfulAuthentication() {
        var auth = new Auth();
        auth.setApiKey(new String(Base64.getEncoder().encode(USER_ID.getBytes(StandardCharsets.UTF_8))));
        auth.setUserId(USER_ID);

        when(servletRequest.getHeader(AUTHORIZATION)).thenReturn(auth.getApiKey());
        when(servletRequest.getRequestURI()).thenReturn(CONTEXT_PATH+"/scooters");
        when(authRepository.findById(any(String.class))).thenReturn(null);

        assertFalse(authFilter.validRequest(servletRequest, servletResponse));
    }

}
