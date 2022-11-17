package ee.kristofer.rental.handler;

import ee.kristofer.rental.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter implements Filter {

    private static final String REGISTRATION_PATH = "/register";


    @Value("${server.servlet.context-path}")
    private String contextPath;


    private final AuthRepository authRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (validRequest(req, res)) {
            chain.doFilter(request, res);
            return;
        }
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    public boolean validRequest(HttpServletRequest req, HttpServletResponse res) {

        if (req.getRequestURI().startsWith(contextPath + REGISTRATION_PATH)) {
            return true;
        }

        var apiKey = req.getHeader(AUTHORIZATION);
        String userId;
        try {
            userId = new String(Base64.getDecoder().decode(apiKey));
        } catch (IllegalArgumentException iae) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return Objects.nonNull(authRepository.findById(userId));
    }

}
