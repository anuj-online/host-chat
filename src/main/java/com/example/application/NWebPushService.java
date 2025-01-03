//package com.example.application;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.webpush.Notification;
//import org.webpush.WebPushException;
//import org.webpush.VapidCredentials;
//import org.webpush.VapidUtils;
//
//import java.io.IOException;
//import java.util.List;
//
//@Service
//public class NWebPushService {
//
//    @Autowired
//    private SubscriptionRepository subscriptionRepository;
//
//    // VAPID credentials
//    private final String vapidPublicKey = "YOUR_VAPID_PUBLIC_KEY";
//    private final String vapidPrivateKey = "YOUR_VAPID_PRIVATE_KEY";
//
//    public void sendPushNotification(String topic, String message) {
//        // Get all subscriptions for the topic
//        List<com.example.notifications.Subscription> subscriptions = subscriptionRepository.findByTopic(topic);
//
//        VapidCredentials vapidCredentials = new VapidCredentials(vapidPublicKey, vapidPrivateKey);
//
//        for (com.example.notifications.Subscription subscription : subscriptions) {
//            // Construct the notification payload
//            Notification notification = new Notification(
//                    subscription.getEndpoint(),
//                    subscription.getPublicKey(),
//                    subscription.getAuthKey(),
//                    message
//            );
//
//            notification.setVapidCredentials(vapidCredentials);
//
//            try {
//                notification.send();
//            } catch (WebPushException | IOException e) {
//                e.printStackTrace();  // Handle error
//            }
//        }
//    }
//}
