package com.example.application.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByTopic(String topic);  // Get subscriptions by topic

    Optional<Subscription> findByUserName(String userName);

    void deleteByUserName(String userName);
}