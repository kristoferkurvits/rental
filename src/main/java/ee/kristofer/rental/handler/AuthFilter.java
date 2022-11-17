package ee.kristofer.rental.handler;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    private static final String REGISTRATION_PATH = "/register";

    @Value("#{server.servlet.context-path}")
    private String contextPath;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (validRequest(req)) {
            chain.doFilter(request, res);
        }
    }

    private boolean validRequest(HttpServletRequest req) {
        if (req.getRequestURI().startsWith(contextPath + REGISTRATION_PATH)) {
            return true;
        }
        //TODO check if user exists in redis cache
        return false;
    }
}
