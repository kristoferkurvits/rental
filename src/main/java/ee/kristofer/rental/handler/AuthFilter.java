package ee.kristofer.rental.handler;

import ee.kristofer.rental.repository.UserRepository;
import ee.kristofer.rental.util.StringUtil;
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

import static ee.kristofer.rental.constants.Constants.AUTHORIZATION;

@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter implements Filter {

    public static final String REGISTRATION_PATH = "/register";


    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final UserRepository userRepository;

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

        if (unauthorizedPath(req)) {
            return true;
        }

        var apiKey = req.getHeader(AUTHORIZATION);
        if (StringUtil.isNullOrEmpty(apiKey)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String userId;
        try {
            userId = new String(Base64.getDecoder().decode(apiKey));
        } catch (IllegalArgumentException iae) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

         return userRepository.findById(userId).isPresent();
    }

    private boolean unauthorizedPath(HttpServletRequest req) {

        return req.getRequestURI().startsWith(contextPath + REGISTRATION_PATH)
                || req.getRequestURI().contains("swagger") || req.getRequestURI().contains("v2/api-docs");
    }

}
