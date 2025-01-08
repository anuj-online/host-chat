package com.example.application.repo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@Accessors(chain = true)
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long topicId;

    private String channelName;

    @ManyToMany
    private List<Subscription> subscriptions = new ArrayList<>();

}
