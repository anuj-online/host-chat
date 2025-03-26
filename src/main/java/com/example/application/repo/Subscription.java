package com.example.application.repo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table
@Getter
@Setter
@Accessors(chain = true)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String userName;
    private String endpoint;  // The push subscription endpoint
    private String publicKey;  // The user's public key
    private String authKey;    // The user's auth secret key
    private String topic;      // The topic this user is subscribed to
    private String password;
    private String deviceId;
}
