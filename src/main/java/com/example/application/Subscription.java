//package com.example.notifications;
//
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
//@Entity
//public class Subscription {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String endpoint;  // The push subscription endpoint
//    private String publicKey;  // The user's public key
//    private String authKey;    // The user's auth secret key
//    private String topic;      // The topic this user is subscribed to
//
//    // Getters and setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getEndpoint() {
//        return endpoint;
//    }
//
//    public void setEndpoint(String endpoint) {
//        this.endpoint = endpoint;
//    }
//
//    public String getPublicKey() {
//        return publicKey;
//    }
//
//    public void setPublicKey(String publicKey) {
//        this.publicKey = publicKey;
//    }
//
//    public String getAuthKey() {
//        return authKey;
//    }
//
//    public void setAuthKey(String authKey) {
//        this.authKey = authKey;
//    }
//
//    public String getTopic() {
//        return topic;
//    }
//
//    public void setTopic(String topic) {
//        this.topic = topic;
//    }
//}
