package com.drestive.chatalot.domain.profile;

import com.drestive.chatalot.domain.common.base.GeneratedIdValueObject;
import com.drestive.chatalot.domain.identity.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mustafa on 24/04/2016.
 */
@Entity
@Table(name = "user_profile")
public class UserProfile extends GeneratedIdValueObject {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected User user;

    @Column(name = "name")
    protected String name;

    @Lob
    @Column(name = "profile_picture")
    protected String profilePicture;

    @Column(name = "profile_picture_type")
    protected String profilePictureType;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "first_user_profile_id"),
            inverseJoinColumns = @JoinColumn(name = "second_user_profile_id"))
    protected Set<UserProfile> friends = new HashSet<>();

    @OneToMany(mappedBy = "from")
    protected List<Invite> invites = new ArrayList<>();

    @OneToMany(mappedBy = "to")
    protected List<Invite> requested = new ArrayList<>();

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected ChatStatus chatStatus;

    @PrePersist
    protected void prePersist() {
        setChatStatus(new ChatStatus(false, "", this));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addFriend(UserProfile userProfile) {
        friends.add(userProfile);
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public List<Invite> getRequested() {
        return requested;
    }

    public Set<UserProfile> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserProfile> friends) {
        this.friends = friends;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public void setRequested(List<Invite> requested) {
        this.requested = requested;
    }

    public ChatStatus getChatStatus() {
        if (chatStatus == null) {
            chatStatus = new ChatStatus(false, "", this);
        }
        return chatStatus;
    }

    protected void setChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public void setOnline(String sessionId) {
        getChatStatus().setSessionId(sessionId);
        getChatStatus().setOnline(true);
    }

    public void setOffline() {
        if (getChatStatus() == null) {
            setChatStatus(new ChatStatus(false, "", this));
        } else {
            getChatStatus().setSessionId("");
            getChatStatus().setOnline(false);
        }
    }

    public Boolean isOnline() {
        return getChatStatus().getOnline();
    }
}
