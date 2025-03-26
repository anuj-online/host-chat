package com.example.application.util;

import com.vaadin.flow.server.VaadinSession;

public final class SessionStorage {
    private SessionStorage() {
    }

    public static String get(String key) {
        return VaadinSession.getCurrent().getAttribute(key).toString();
    }
    public static void store(String key, String value){
        VaadinSession.getCurrent().setAttribute(key, value);
    }
}
