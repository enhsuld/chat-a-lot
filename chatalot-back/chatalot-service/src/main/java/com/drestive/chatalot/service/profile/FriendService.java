package com.drestive.chatalot.service.profile;

import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.service.chat.dto.ControlMessage;
import com.drestive.chatalot.service.chat.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by mustafa on 02/06/2016.
 */
@Service
public class FriendService {

    public static final String CHAT_USERS_TOPIC_PREFIX = "/topic/users/";

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * Notify this user's friends of his sub/unsub events.
     *
     * @param userProfile user profile of user that triggered this messaging event
     */
    public void broadCastFriendEvent(UserProfile userProfile) {
        userProfile.getFriends().stream().filter(UserProfile::isOnline)
                .forEach(friend -> sendMessageToUser(friend, ControlMessage.REFRESH_FRIENDS_COMMAND));
    }

    public void sendMessageToUser(UserProfile userProfile, Message message) {
        template.convertAndSend(CHAT_USERS_TOPIC_PREFIX + userProfile.getUser().getUsername(), message);
    }
}
