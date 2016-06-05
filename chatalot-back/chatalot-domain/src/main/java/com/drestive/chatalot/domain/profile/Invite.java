package com.drestive.chatalot.domain.profile;

import com.drestive.chatalot.domain.common.base.GeneratedIdValueObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mustafa on 25/05/2016.
 */
@Entity
@Table(name = "invite")
public class Invite extends GeneratedIdValueObject {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "from_user_profile_id")
    protected UserProfile from;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "to_user_profile_id")
    protected UserProfile to;

    @Column(name = "created_on")
    protected Date createdOn;

    @Column(name = "accepted")
    protected Boolean accepted;

    public Invite() {
    }

    public Invite(UserProfile from, UserProfile to, Date createdOn) {
        this.from = from;
        this.to = to;
        this.createdOn = createdOn;
    }

    @PrePersist
    protected void prePersist() {
        accepted = false;
    }

    public UserProfile getFrom() {
        return from;
    }

    public void setFrom(UserProfile from) {
        this.from = from;
    }

    public UserProfile getTo() {
        return to;
    }

    public void setTo(UserProfile to) {
        this.to = to;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
