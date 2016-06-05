package com.drestive.chatalot.security.service;

import com.drestive.chatalot.security.model.RestAuthenticationRequestPrincipal;
import com.drestive.chatalot.security.model.RestAuthenticationToken;
import com.drestive.chatalot.security.model.RestUserDetails;
import com.drestive.chatalot.service.identity.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Authentication provider service for common
 * provider related operations.
 */
@Service
public class AuthenticationProviderService {

    @Autowired
    ApiUserDetailsService userDetailsService;

    public Authentication postProcess(RestAuthenticationToken restAuthenticationToken,
                                      RestAuthenticationRequestPrincipal principal) {
        RestAuthenticationToken result;

        RestUserDetails restUserDetails = (RestUserDetails) userDetailsService.loadUserByUsername(principal.getUsername());

        if (restUserDetails == null) {
            throw new UsernameNotFoundException("User not found: " + principal.getUsername());
        } else {

            if (!restUserDetails.isEnabled()) {
                throw new DisabledException("User is disabled.");
            }

            String xAuthToken = UUID.randomUUID().toString();
            restAuthenticationToken.setxAuthToken(xAuthToken);

            Map<String, String> authenticationDetails = new HashMap<>();
            authenticationDetails.put(Constants.HEADER_X_AUTH_TOKEN, xAuthToken);
            authenticationDetails.put(Constants.HEADER_X_AUTH_ROLES,
                    restUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")));

            result = new RestAuthenticationToken(restUserDetails, restUserDetails.getAuthorities());
            result.setDetails(authenticationDetails);
        }

        return result;
    }

}
