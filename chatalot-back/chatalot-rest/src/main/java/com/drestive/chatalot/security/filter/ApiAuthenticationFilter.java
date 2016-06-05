package com.drestive.chatalot.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.drestive.chatalot.security.model.RestAuthenticationRequestPrincipal;
import com.drestive.chatalot.security.model.RestAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter for API endpoint.
 */
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger log = LoggerFactory.getLogger(ApiAuthenticationFilter.class);

    public ApiAuthenticationFilter() {
        super("/j_spring_security_check");
        log.info("ApiAuthenticationFilter init");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // parse input variables
        ObjectMapper objectMapper = new ObjectMapper();
        RestAuthenticationRequestPrincipal principal = objectMapper
                .readValue(request.getInputStream(), RestAuthenticationRequestPrincipal.class);

        RestAuthenticationToken authRequest = new RestAuthenticationToken(principal);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
