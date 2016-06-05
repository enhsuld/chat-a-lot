package com.drestive.chatalot.service.identity.model;

import com.drestive.chatalot.domain.identity.User;

import java.util.Map;

/**
 * AuthenticationDetails model to store information about the 
 * authenticated user.
 */
public class CachedAuthenticationDetails {

    private User user;
    private Map<String, String> authenticationDetails;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, String> getAuthenticationDetails() {
        return authenticationDetails;
    }

    public void setAuthenticationDetails(
            Map<String, String> authenticationDetails) {
        this.authenticationDetails = authenticationDetails;
    }

}
