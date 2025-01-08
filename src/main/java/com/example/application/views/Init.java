package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("")
public class Init extends Div {
    private final UI ui = UI.getCurrent();

    // intentionally left here, so when we have to draw an installation route, we start from here.
    public Init() {
        ui.navigateToClient("chat");
    }
}
