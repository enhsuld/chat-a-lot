package com.drestive.chatalot.security.provider;

import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.security.model.RestAuthenticationRequestPrincipal;
import com.drestive.chatalot.security.model.RestAuthenticationToken;
import com.drestive.chatalot.security.service.AuthenticationProviderService;
import com.drestive.chatalot.service.identity.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Authentication provider that is queried in turn to see if it can
 * perform authentication.
 */
public class RestAuthenticationProvider implements AuthenticationProvider, InitializingBean, Ordered {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final static int NON_ORDERED = -1;
    private UserDetailsService userDetailsService;

    private IdentityService identityService;

    private AuthenticationProviderService authenticationProviderService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        RestAuthenticationToken restAuthenticationToken = (RestAuthenticationToken) authentication;
        RestAuthenticationRequestPrincipal principal = (RestAuthenticationRequestPrincipal) restAuthenticationToken
                .getPrincipal();

        log.info("[AUTHENTICATION] [API_AUTH] [USERNAME={}]", principal.getUsername());

        if (!StringUtils.hasText(principal.getUsername()) || !StringUtils.hasText(principal.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(
                    "No credentials found! Please send a username and a password with your request.");
        }

        User user = identityService.findUserByUsername(principal.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException(principal.getUsername());
        }

        boolean passwordMatches = user.checkPassword(principal.getPassword());
        if (!passwordMatches) {
            throw new BadCredentialsException("Username or password does not match");
        }

        return authenticationProviderService.postProcess(restAuthenticationToken, principal);
    }

    @Override
    public int getOrder() {
        return NON_ORDERED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDetailsService, "UserDetailsService property must be set!");
        Assert.notNull(identityService, "OrganizationService property must be set!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RestAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    public void setAuthenticationProviderService(AuthenticationProviderService authenticationProviderService) {
        this.authenticationProviderService = authenticationProviderService;
    }

}
