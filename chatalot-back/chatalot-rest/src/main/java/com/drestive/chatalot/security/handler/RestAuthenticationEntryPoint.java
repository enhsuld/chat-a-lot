package com.drestive.chatalot.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication entry point that is called if the user requests a secure
 * HTTP resource but they are not authenticated.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String REMEMBER_ME_REQUEST_URI = "/api/auth/rememberMe";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

}
