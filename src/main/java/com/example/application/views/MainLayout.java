package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;

public class MainLayout extends AppLayout {

    public MainLayout() {
        var themingView = new ThemingView();
        var changeTheme = new Button("Theme", e -> themingView.open());
        addToNavbar(changeTheme, themingView);
    }
}
