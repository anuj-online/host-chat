//package com.example.application;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//import com.example.notifications.Subscription;
//
//public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
//    List<Subscription> findByTopic(String topic);  // Get subscriptions by topic
//}