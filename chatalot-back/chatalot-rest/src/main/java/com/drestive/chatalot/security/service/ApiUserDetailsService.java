package com.drestive.chatalot.security.service;

import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.security.model.RestUserDetails;
import com.drestive.chatalot.service.identity.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service for Spring Security.
 */
@Service
public class ApiUserDetailsService implements UserDetailsService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IdentityService identityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = identityService.findUserByUsername(username);

        // check if the username is found on user base
        if (user == null) {
            log.error("[AUTHENTICATION] [USERNAME_NOT_FOUND] [USERNAME_DOES_NOT_EXIST_ON_DB] [USERNAME={}]", username);
            return null;
        }

        return new RestUserDetails(user);
    }

}
