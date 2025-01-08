package com.example.application.service;

import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.WebStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatPersistService implements CollaborationMessagePersister {
    private final Map<String, List<CollaborationMessage>> messagesByTopic = new HashMap<>();

    private final WebPushService webPushService;

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {

        String topicId = query.getTopicId();
        Instant since = query.getSince();

        // Fetch the messages for the given topicId
        List<CollaborationMessage> messages = messagesByTopic.getOrDefault(topicId, new ArrayList<>());

        // Filter messages that are newer than the 'since' timestamp
        return messages.stream()
                .filter(msg -> msg.getTime().equals(since) || msg.getTime().isAfter(since));
    }

    @Override
    public void persistMessage(PersistRequest request) {
        String topicId = request.getTopicId();
        CollaborationMessage message = request.getMessage();
        webPushService.notifyAll(topicId, request.getMessage().getText());
        // Add the message to the in-memory storage
        messagesByTopic.computeIfAbsent(topicId, k -> new ArrayList<>()).add(message);
    }
}
