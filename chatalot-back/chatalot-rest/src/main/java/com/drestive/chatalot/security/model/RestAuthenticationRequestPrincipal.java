package com.drestive.chatalot.security.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.security.Principal;

/**
 * Represents authentication request model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestAuthenticationRequestPrincipal implements Principal {

    @JsonProperty
    private String username;

    @JsonDeserialize
    private String password;

    @JsonSerialize
    private String xAuthToken;

    public RestAuthenticationRequestPrincipal() {
    }

    @Override
    public String getName() {
        return getUsername();
    }

    public RestAuthenticationRequestPrincipal(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getxAuthToken() {
        return xAuthToken;
    }

    public void setxAuthToken(String xAuthToken) {
        this.xAuthToken = xAuthToken;
    }

}
