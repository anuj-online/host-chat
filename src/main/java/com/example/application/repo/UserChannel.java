package com.example.application.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class UserChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String channelName; // Name of the channel (e.g., group or generated for one-to-one)

    @Column(nullable = false)
    private boolean isGroup; // Flag to determine if the channel is a group or one-to-one

    @ManyToMany
    @JoinTable(name = "user_channel_mapping", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Subscription> users; // Participants in the channel (2 for one-to-one)

}
