package com.example.application.views;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public final class NotificationHelper {
    private static final Notification notification = new Notification();

    static {
        notification.setPosition(Notification.Position.TOP_STRETCH);
    }

    private NotificationHelper() {
    }


    public static void showError(String message) {
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
        notification.setText(message);
        notification.open();
    }

    public static void showSuccess(String message) {
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setText(message);
        notification.open();
    }
}
