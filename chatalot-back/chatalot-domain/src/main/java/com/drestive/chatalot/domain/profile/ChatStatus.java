package com.drestive.chatalot.domain.profile;

import com.drestive.chatalot.domain.common.base.GeneratedIdValueObject;

import javax.persistence.*;

/**
 * Created by mustafa on 27/05/2016.
 */
@Entity
@Table(name = "chat_status")
public class ChatStatus extends GeneratedIdValueObject {

    @Column(name = "online")
    protected Boolean online;

    @Column(name = "session_id")
    protected String sessionId;

    @OneToOne
    @JoinColumn(name="user_profile_id")
    protected UserProfile userProfile;

    protected ChatStatus() {
    }

    protected ChatStatus(Boolean online, String sessionId, UserProfile userProfile) {
        this.online = online;
        this.sessionId = sessionId;
        this.userProfile = userProfile;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
