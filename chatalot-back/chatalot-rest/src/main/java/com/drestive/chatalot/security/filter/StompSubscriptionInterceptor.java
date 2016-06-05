package com.drestive.chatalot.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.security.core.Authentication;

/**
 * Created by mustafa on 04/06/2016.
 */
public class StompSubscriptionInterceptor extends ChannelInterceptorAdapter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public final static String USER_DEST_PREFIX = "/topic/users/";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        if (message.getHeaders().get(SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER).equals(SimpMessageType.SUBSCRIBE)) {

            Object user = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER);
            Authentication authentication;
            if ((user instanceof Authentication)) {
                authentication = (Authentication) user;
            } else {
                log.warn(String.format("User unauthenticated."));
                throw new IllegalArgumentException("Illegal attempt to subscribe to topic.");
            }

            String destination = (String) message.getHeaders().get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
            //Allow subscription to the user's own receive topic only
            if (!destination.equals(USER_DEST_PREFIX + authentication.getName())) {
                log.warn(String.format("Illegal subscription, from user \"%s\", to destination \"%s\"",
                        authentication.getName(), destination));
                throw new IllegalArgumentException("Illegal attempt to subscribe to topic.");
            }
        }
        return super.preSend(message, channel);
    }
}
