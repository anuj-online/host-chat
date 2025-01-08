package com.example.application.service;

import com.example.application.repo.Subscription;
import com.example.application.repo.SubscriptionRepository;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.webpush.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebPushService {

    private final SubscriptionRepository subscriptionRepository;
    WebPush webPush;
    @Value("${public.key}")
    private String publicKey;
    @Value("${private.key}")
    private String privateKey;
    @Value("${subject}")
    private String subject;

    private static String getUserName() {
        return VaadinSession.getCurrent().getAttribute("user-name").toString();
    }

    /**
     * Initialize security and push service for initial get request.
     *
     * @throws GeneralSecurityException security exception for security complications
     */
    public WebPush getWebPush() {
        if (webPush == null) {
            webPush = new WebPush(publicKey, privateKey, subject);
        }
        return webPush;
    }

    /**
     * Send a notification to all subscriptions.
     *
     * @param title message title
     * @param body  message body
     */
    public void notifyAll(String title, String body) {
        String userName = getUserName();
        subscriptionRepository.findAll().stream().filter(subscription -> !subscription.getUserName().equals(userName)).forEach(subscription -> sendNotification(new WebPushSubscription(subscription.getEndpoint(), new WebPushKeys(subscription.getPublicKey(), subscription.getAuthKey())), "Message from " + userName, body));
    }

    private void sendNotification(WebPushSubscription subscription, String userName, String body) {
        try {
            webPush.sendNotification(subscription, new WebPushMessage(userName, body));
        } catch (WebPushException e) {
            log.error("Subscription expired.", e);
            subscriptionRepository.deleteByUserName(userName);
        }
    }

    public void store(WebPushSubscription subscription) {
        log.info("Subscribed to {}", subscription.endpoint());
        log.info("keys {}", subscription.keys());
        var userSubscription = new Subscription().setTopic("chat").setEndpoint(subscription.endpoint()).setUserName(getUserName()).setAuthKey(subscription.keys().auth()).setPublicKey(subscription.keys().p256dh());

        subscriptionRepository.save(userSubscription);
        /*
         * Note, in a real world app you'll want to persist these
         * in the backend. Also, you probably want to know which
         * subscription belongs to which user to send custom messages
         * for different users. In this demo, we'll just use
         * endpoint URL as key to store subscriptions in memory.
         */
    }

    public void remove(WebPushSubscription subscription) {
        log.info("Unsubscribed {}", subscription.endpoint());
        subscriptionRepository.findByUserName(getUserName()).ifPresent(subscriptionRepository::delete);
    }

    public boolean isEmpty() {
        return subscriptionRepository.findByUserName(getUserName()).isEmpty();
    }

}