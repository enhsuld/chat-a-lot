package com.drestive.chatalot.domain.identity;

import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.domain.common.base.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "chatalot_user")
public class User extends AbstractEntity implements Serializable {

    @Column(name = "username", unique = true, nullable = false)
    protected String username;

    @Column(name = "password", nullable = false)
    protected String password;

    @Column(name = "email_address",  unique = true, nullable = false)
    protected String emailAddress;

    @Column(name = "is_expired", nullable = false)
    protected Boolean expired;

    @Column(name = "is_locked", nullable = false)
    protected Boolean locked;

    @Column(name = "is_enabled", nullable = false)
    protected Boolean enabled;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    protected UserProfile userProfile;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = true)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", nullable = false, updatable = true)})
    protected List<Role> roles = new ArrayList<>();

    @Transient
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.setPassword(password);
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role){
        getRoles().add(role);
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean hasAnyOf(List<Role> roles) {
        List<Role> foundRoles = getRoles().stream().filter(role -> roles.contains(role)).collect(Collectors.toList());
        return foundRoles.size() > 0;
    }

    public Boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRolesList() {
        ArrayList<String> roles = new ArrayList<>();
        this.getRoles().stream().forEach(role -> roles.add(role.getId()));
        return roles;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean checkPassword(String passwordToCheck) {
        if (passwordToCheck == null) {
            return false;
        }

        try {
            if (getPasswordDigest(passwordToCheck).equals(password)) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Unable to check user password for user: " + getUsername());
        }

        return false;
    }

    public void setPassword(String passwordToReset) {
        if (passwordToReset == null || passwordToReset.isEmpty()) {
            throw new IllegalArgumentException("Password string must not be null or empty.");
        }

        try {
            password = getPasswordDigest(passwordToReset);
        } catch (Exception e) {
            logger.error("Unable to set user password for user: " + getUsername());
        }
    }

    public String getPasswordDigest(String passwordStr) throws Exception {
        if (passwordStr == null) {
            throw new IllegalArgumentException("String parameter can't be null.");
        }

        try {
            byte[] hashedPw = MessageDigest.getInstance("SHA-256")
                    .digest((passwordStr + "saltsaltsalt123123123.").getBytes());
            return new BigInteger(1, hashedPw).toString(16); //store as hex string
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception("Cannot hash user password", ex);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
