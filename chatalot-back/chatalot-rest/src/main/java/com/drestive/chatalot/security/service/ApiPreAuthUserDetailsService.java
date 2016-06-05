package com.drestive.chatalot.security.service;

import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.security.model.RestAuthenticationRequestPrincipal;
import com.drestive.chatalot.security.model.RestUserDetails;
import com.drestive.chatalot.service.identity.TokenCacheService;
import com.drestive.chatalot.service.identity.model.CachedAuthenticationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * User details service for pre authenticated users via X-Auth-Token.
 */
@Service
public class ApiPreAuthUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    TokenCacheService tokenCacheService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        RestAuthenticationRequestPrincipal principal = (RestAuthenticationRequestPrincipal) token.getPrincipal();
        String xAuthToken = principal.getxAuthToken();

        CachedAuthenticationDetails cachedAuthenticationDetails = tokenCacheService.getUserDetails(xAuthToken);

        if (cachedAuthenticationDetails == null || cachedAuthenticationDetails.getUser() == null) {
            throw new UsernameNotFoundException("Token not found : " + xAuthToken);
        }

        token.setDetails(cachedAuthenticationDetails.getAuthenticationDetails());

        User user = cachedAuthenticationDetails.getUser();

        RestUserDetails userDetails = new RestUserDetails(user);

        return userDetails;
    }

}
