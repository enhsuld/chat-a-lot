package com.drestive.chatalot.service.register.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by mustafa on 02/05/2016.
 */
public class Registration implements Serializable {

    @NotNull(message = "Name must be provided.")
    protected String name;

    @NotNull(message = "User name must be provided.")
    protected String username;

    @NotNull(message = "Password must be provided.")
    @Length(min = 8, max = 80)
    protected String password;

    @NotNull(message = "Email address must be provided.")
    @Email
    protected String emailAddress;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
