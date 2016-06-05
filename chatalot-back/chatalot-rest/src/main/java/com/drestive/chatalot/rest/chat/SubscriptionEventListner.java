package com.drestive.chatalot.rest.chat;

import com.drestive.chatalot.domain.profile.UserProfile;
import com.drestive.chatalot.domain.profile.repository.UserProfileRepository;
import com.drestive.chatalot.service.profile.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by mustafa on 26/05/2016.
 */

/**
 * Listener that manages the user status (online/offline) based on messaging events that
 * occur in the application. It also notifies users of their friends' status changes.
 */
@Component
public class SubscriptionEventListner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    protected UserProfileRepository userProfileRepository;

    @Autowired
    protected FriendService friendService;

    /**
     * A subscription event occurred. Find the related user and update its status in the db.
     * Notify its friends of the event.
     *
     * @param subscription event
     */
    @Transactional
    @EventListener
    public void onSubscriptionEvent(SessionSubscribeEvent subscription) {

        for (String headerKey : subscription.getMessage().getHeaders().keySet()) {
            log.trace("Header: " + headerKey + " - " + subscription.getMessage().getHeaders().get(headerKey));
        }

        String destination = (String) subscription.getMessage().getHeaders()
                .get(SimpMessageHeaderAccessor.DESTINATION_HEADER);
        String sessionId = (String) subscription.getMessage().getHeaders()
                .get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);

        String[] parts = destination.replaceFirst("^/", "").split("/");
        if (parts.length < 3) {
            return;
        }

        String username = parts[2];

        UserProfile userProfile = userProfileRepository.getByUsername(subscription.getUser().getName());
        if (userProfile == null) {
            return;
        }

        userProfile.setOnline(sessionId);
        entityManager.persist(userProfile);
        friendService.broadCastFriendEvent(userProfile);
    }

    /**
     * An unsubscription event occurred. Find the related user and update its status in the db.
     * Notify its friends of the event.
     *
     * @param unsubscription event
     */
    @Transactional
    @EventListener
    public void onUnsubscriptionEvent(SessionUnsubscribeEvent unsubscription) {
        for (String headerKey : unsubscription.getMessage().getHeaders().keySet()) {
            log.trace("Header: " + headerKey + " - " + unsubscription.getMessage().getHeaders().get(headerKey));
        }
        processOfflineEvents(unsubscription);
    }

    /**
     * Socket connection to a user was dropped. Update the user's status in the db and notify its friends.
     *
     * @param disconnect event
     */
    @Transactional
    @EventListener
    public void onSessionDisconnectEvent(SessionDisconnectEvent disconnect) {
        for (String headerKey : disconnect.getMessage().getHeaders().keySet()) {
            log.trace("Header: " + headerKey + " - " + disconnect.getMessage().getHeaders().get(headerKey));
        }

        processOfflineEvents(disconnect);
    }

    /**
     * Method that processes events that cause the user to go offline.
     *
     * @param event that causes the client to go offline
     */
    protected void processOfflineEvents(AbstractSubProtocolEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");

        UserProfile userProfile = userProfileRepository.getByChatSessionId(sessionId);
        if (userProfile == null) {
            return;
        }
        userProfile.setOffline();
        entityManager.persist(userProfile);
        friendService.broadCastFriendEvent(userProfile);
    }
}