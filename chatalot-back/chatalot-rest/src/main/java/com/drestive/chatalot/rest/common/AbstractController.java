package com.drestive.chatalot.rest.common;

import com.drestive.chatalot.domain.identity.User;
import com.drestive.chatalot.domain.identity.repository.UserRepository;
import com.drestive.chatalot.security.model.RestUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Holds logic and data that's common to all controllers.
 */
abstract public class AbstractController {

    @Autowired
    UserRepository userRepository;

    public String getCurrentUsername() {
        RestUserDetails restUserDetails = (RestUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return restUserDetails.getUsername();
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.getByUsername(username);
    }
}
