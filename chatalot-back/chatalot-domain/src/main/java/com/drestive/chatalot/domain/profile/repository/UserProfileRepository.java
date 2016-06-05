package com.drestive.chatalot.domain.profile.repository;

import com.drestive.chatalot.domain.common.base.AbstractRepository;
import com.drestive.chatalot.domain.profile.UserProfile;
import org.springframework.stereotype.Repository;

/**
 * Created by mustafa on 02/05/2016.
 */
@Repository
public class UserProfileRepository extends AbstractRepository<UserProfile> {
    public UserProfile getByUsername(String username) {
        return getFirstBy("user.username", username);
    }

    public UserProfile getByChatSessionId(String sessionId) {
        return getFirstBy("chatStatus.sessionId", sessionId);
    }
}
