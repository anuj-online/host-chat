package com.example.application.repo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private UserChannel channel; // The channel where the message was sent

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Subscription sender; // The sender of the message

    @Column(nullable = false)
    private String content; // The message content

    @Column(nullable = false)
    private LocalDateTime timestamp; // The time when the message was sent

}
