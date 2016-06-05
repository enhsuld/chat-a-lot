package com.drestive.chatalot.rest.chat;

import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.domain.profile.repository.UserProfileRepository;
import com.drestive.chatalot.service.chat.dto.ChatMessage;
import com.drestive.chatalot.service.identity.TokenCacheService;
import com.drestive.chatalot.service.profile.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by mustafa on 26/05/2016.
 */

/**
 * This is the controller that handles chat requests.
 */
@Controller
@RequestMapping("/")
public class ChatController {

    public static final String CHAT_SEND_TOPIC = "/send";

    private final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    protected UserProfileRepository userProfileRepository;

    @Autowired
    protected TokenCacheService tokenCacheService;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    protected FriendService friendService;

    /**
     * Listens for chat messages from users and forwards them to their recipients specified in
     * the "to" property. It ensures that messages are sent between friends only.
     * Messages are currently not persisted.
     *
     * @param message
     */
    @MessageMapping(CHAT_SEND_TOPIC)
    @Transactional
    public void sendMessage(ChatMessage message, Principal principal) {
        String toUsername = message.getTo();

        UserProfile fromUser = userProfileRepository.getByUsername(principal.getName());

        UserProfile toUser = userProfileRepository.getByUsername(toUsername);

        if (fromUser == null || toUser == null || !toUser.isOnline()) {
            return;
        }

        //check if target user is a friend of 'from' user
        if (!fromUser.getFriends().contains(toUser)) {
            return;
        }

        message.setType(ChatMessage.CHAT_MESSAGE);
        message.setFrom(fromUser.getUser().getUsername());

        friendService.sendMessageToUser(toUser, message);
    }
}
