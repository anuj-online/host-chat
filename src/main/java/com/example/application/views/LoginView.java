package com.example.application.views;

import com.example.application.service.WebPushService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.UUID;

@Route("login")
public class LoginView extends LoginOverlay {

    private static final List<User> users = List.of(new User("u", "u"), new User("a", "a"));

    public LoginView(WebPushService webPushService) {
        WebStorage.getItem("login", value -> {
            if (value != null && value.equals("logged-in")) {
                UI.getCurrent().navigate(ChatView.class);
            } else {
                this.setOpened(true);
                addLoginListener(event -> {
                    var confirmDialog = new Dialog();
                    Button cancel = new Button("Cancel");
                    Button button = new Button("I Agree");
                    confirmDialog.add(new VerticalLayout(new NativeLabel("Any other devices associated with the account will be removed.")), new HorizontalLayout(button, cancel));
                    confirmDialog.open();
                    cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
                    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    button.addClickListener(event1 -> {
                        confirmDialog.close();
                        if (validUser(event)) {
                            loginAction(webPushService, event);
                        }
                    });

                });
            }
        });

    }

    private static void loginAction(WebPushService webPushService, LoginEvent event) {
        WebStorage.setItem("login", "logged-in");
        WebStorage.setItem("user-name", event.getUsername());
        VaadinSession.getCurrent().setAttribute("user-name", event.getUsername());
        VaadinSession.getCurrent().setAttribute("device-id", UUID.randomUUID().toString()); //TODO generating deviceID at login
        Notification.show("Login Successful", 4000, Notification.Position.TOP_CENTER);
        webPushService.getWebPush().subscribe(UI.getCurrent(), webPushService::store);
        UI.getCurrent().navigate(ChatView.class);
    }

    private static boolean validUser(LoginEvent event) {
        return users.stream().anyMatch(user -> event.getUsername().equals(user.userName()) && event.getPassword().equals(user.password()));
    }

    record User(String userName, String password) {
    }
}
