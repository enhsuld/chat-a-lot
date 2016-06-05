package com.drestive.chatalot.security.handler;

import com.drestive.chatalot.security.model.RestUserDetails;
import com.drestive.chatalot.service.identity.Constants;
import com.drestive.chatalot.service.identity.TokenCacheService;
import com.drestive.chatalot.service.identity.model.CachedAuthenticationDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Authentication handler that triggers after a successful login.
 */
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TokenCacheService tokenCacheService;

    @SuppressWarnings("unchecked")
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);

        RestUserDetails restUserDetails = (RestUserDetails) authentication.getPrincipal();
        Map<String, String> authenticationDetails = (Map<String, String>) authentication.getDetails();
        String xAuthToken = authenticationDetails.get(Constants.HEADER_X_AUTH_TOKEN);
        String xAuthRoles = authenticationDetails.get(Constants.HEADER_X_AUTH_ROLES);

        CachedAuthenticationDetails cachedAuthenticationDetails = new CachedAuthenticationDetails();
        cachedAuthenticationDetails.setUser(restUserDetails.getUser());
        cachedAuthenticationDetails.setAuthenticationDetails(authenticationDetails);

        tokenCacheService.saveToken(xAuthToken, cachedAuthenticationDetails);

        response.setHeader(Constants.HEADER_X_AUTH_TOKEN, xAuthToken);
        response.setHeader(Constants.HEADER_X_AUTH_ROLES, xAuthRoles);
    }

}
