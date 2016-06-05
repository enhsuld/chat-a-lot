package com.drestive.chatalot.service.profile.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by mustafa on 05/05/2016.
 */

// To deal with cyclic dependencies
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class UserProfileDTO {

    @JsonView(ProfileViews.ProfileShort.class)
    protected Integer id;

    @JsonView(ProfileViews.ProfileShort.class)
    protected String username;

    @NotNull
    @JsonView(ProfileViews.ProfileShort.class)
    protected String name;

    @NotNull
    @Email
    @JsonView(ProfileViews.ProfileShort.class)
    protected String emailAddress;

    @JsonView(ProfileViews.ProfileFull.class)
    protected String profilePicture;

    @JsonView(ProfileViews.ProfileFull.class)
    protected String profilePictureType;

    @JsonView(ProfileViews.ProfileFull.class)
    protected List<UserProfileDTO> friends;

    @JsonView(ProfileViews.ProfileFull.class)
    protected List<InviteDTO> invites;

    @JsonView(ProfileViews.ProfileFull.class)
    protected List<InviteDTO> requested;

    @JsonView(ProfileViews.ProfileChat.class)
    protected Boolean online;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureType() {
        return profilePictureType;
    }

    public void setProfilePictureType(String profilePictureType) {
        this.profilePictureType = profilePictureType;
    }

    public List<InviteDTO> getInvites() {
        return invites;
    }

    public void setInvites(List<InviteDTO> invites) {
        this.invites = invites;
    }

    public List<InviteDTO> getRequested() {
        return requested;
    }

    public void setRequested(List<InviteDTO> requested) {
        this.requested = requested;
    }

    public List<UserProfileDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<UserProfileDTO> friends) {
        this.friends = friends;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
