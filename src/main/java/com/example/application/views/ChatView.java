package com.example.application.views;

import com.example.application.service.ChatPersistService;
import com.example.application.service.WebPushService;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.webpush.WebPush;

@Route("")
public class ChatView extends VerticalLayout {
    private final ChatPersistService chatPersistService;
    private final TextField userName = new TextField("Username");
    private final WebPush webPush;
    private final WebPushService webPushService;

    public ChatView(ChatPersistService chatPersistService, WebPushService webPushService) {
        this.chatPersistService = chatPersistService;
        this.webPush = webPushService.getWebPush();
        this.webPushService = webPushService;
        WebStorage.getItem("user-name").whenComplete((s, throwable) -> {
            if (s == null) {
                newUserAction().run();
            } else {
                chat(s);
            }
        });
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        webPush.subscriptionExists(ui, registered -> {
            if (registered && webPushService.isEmpty()) {
                webPush.fetchExistingSubscription(ui, webPushService::store);
            }
        });
    }

    private Runnable newUserAction() {
        return () -> {
            var dialog = new Dialog();
            dialog.add(userName);
            dialog.add(new Button("Start Chat", e -> {
                userName.getOptionalValue().ifPresentOrElse(userName1 -> {
                    chat(userName1);
                    WebStorage.setItem("user-name", userName1);
                    webPush.subscribe(UI.getCurrent(), webPushService::store);
                    dialog.close();
                }, () -> Notification.show("Please enter user"));
            }));
            dialog.open();
        };
    }

    public void chat(String userName) {
        setSizeFull();
        var topic = "discuss-about-movies";
        var user = new UserInfo(userName, userName, "");
        var messages = new CollaborationMessageList(user, topic, chatPersistService);
        messages.setMessageConfigurator((messageListItem, userInfo) -> {
            if (userInfo.equals(user)) {
                messageListItem.addClassNames("from-self");
            } else {
                messageListItem.addClassNames("from-other");
            }
        });
        var input = new CollaborationMessageInput(messages);
        messages.setWidthFull();
        input.setWidthFull();
        add(messages, input);
        expand(messages);
    }
}
