package com.example.application.views;

import com.example.application.service.ChatPersistService;
import com.example.application.service.WebPushService;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Svg;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.webpush.WebPush;
import com.vaadin.flow.theme.lumo.LumoIcon;
import elemental.json.JsonValue;
import lombok.extern.slf4j.Slf4j;

@PageTitle("Chat")
@Route(value = "chat")
@Slf4j
public class ChatView extends VerticalLayout {
    private final ChatPersistService chatPersistService;
    private final TextField userName = new TextField("Username");
    private final WebPush webPush;
    private final WebPushService webPushService;

    public ChatView(ChatPersistService chatPersistService, WebPushService webPushService) {
        this.chatPersistService = chatPersistService;
        this.webPush = webPushService.getWebPush();
        this.webPushService = webPushService;
        this.setWidthFull();
        this.setMaxWidth("815px");
        this.getStyle().set("justify-self", "center");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = UI.getCurrent();
        ui.getPage().executeJs("return window.matchMedia('(display-mode: standalone)').matches ? 'installed' : 'not_installed';").then(jsonValue -> {
            verifyInstallMode(jsonValue, ui);
        });

    }

    private void verifyInstallMode(JsonValue jsonValue, UI ui) {
        log.info("mode {}", jsonValue.asString());
        if (jsonValue.asString().equals("installed")) {
            installed(ui);
        } else {
            UI.getCurrent().getPage().retrieveExtendedClientDetails(r -> {
                H2 h2 = new H2("This PWA works only upon installation.");
                if (r.isIOS() || r.isIPad()) {
                    VerticalLayout verticalLayout = new VerticalLayout();
                    verticalLayout.addClassName("banner");
                    verticalLayout.add(h2);
                    verticalLayout.add(new HorizontalLayout(new NativeLabel("Click on share"), new Svg(ShareIcon.SVG)));
                    verticalLayout.add(new HorizontalLayout(new NativeLabel("Add to Home screen"), LumoIcon.PLUS.create()));
                    add(verticalLayout);
                } else if (r.isTouchDevice()) {
                    add(h2);
                }
            });
        }
    }

    private void installed(UI ui) {
        WebStorage.getItem("user-name").whenComplete((s, throwable) -> {
            log.info("getting username from storage {}", s);
            if (s == null) {
                firstTimeUser(ui, s);
            } else {
                VaadinSession.getCurrent().setAttribute("user-name", s);
                webPush.subscriptionExists(ui, registered -> {
                    if (registered && webPushService.isEmpty()) {
                        webPush.fetchExistingSubscription(ui, subscription -> webPushService.store(subscription));
                    }
                });
                chat(s);
            }
        });
    }

    private void firstTimeUser(UI ui, String s) {
        newUserAction().run();
    }

    private Runnable newUserAction() {
        return () -> {
            var dialog = new Dialog();
            dialog.add(userName);
            dialog.add(new Button("Start Chat", e -> {
                userName.getOptionalValue().ifPresentOrElse(userName1 -> {
                    chat(userName1);
                    VaadinSession.getCurrent().setAttribute("user-name", userName1);
                    WebStorage.setItem("user-name", userName1);
                    webPush.subscribe(UI.getCurrent(), subscription -> webPushService.store(subscription));
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
