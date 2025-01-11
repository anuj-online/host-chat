package com.example.application.views;

import com.vaadin.flow.component.Svg;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import elemental.json.JsonValue;
import lombok.extern.slf4j.Slf4j;

@Route("")
@Slf4j
@JsModule("https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs")
@AnonymousAllowed
public class Init extends Div {
    // intentionally left here, so when we have to draw an installation route, we start from here.
    public Init() {
        this.setMaxWidth("815px");
        this.getStyle().set("justify-self", "center");
        addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.AlignContent.CENTER, LumoUtility.AlignSelf.CENTER, LumoUtility.TextAlignment.CENTER);
        var ui = UI.getCurrent();
        ui.getPage().executeJs("return window.matchMedia('(display-mode: standalone)').matches ? 'installed' : 'not_installed';").then(jsonValue -> {
            verifyInstallMode(jsonValue, ui);
        });
        getElement().setProperty("innerHTML", """
                <dotlottie-player src="https://lottie.host/40628cad-bb97-4389-a563-41b7f381ccff/ldb2wVjSQn.lottie" background="transparent" speed="1" style="width: 300px; height: 300px" loop autoplay></dotlottie-player>
                """);
    }

    private void verifyInstallMode(JsonValue jsonValue, UI ui) {
        add(new H3("Chat app"));
        if (jsonValue.asString().equals("installed")) {
            ui.navigateToClient("login");
        } else {
            UI.getCurrent().getPage().retrieveExtendedClientDetails(r -> {
                var h2 = new H2("Install the App and enable Notifications.");
                var h3 = new H4("That's it. You are done.");
                if (r.isTouchDevice()) {
                    var verticalLayout = new VerticalLayout();
                    verticalLayout.addClassName("banner");
                    verticalLayout.add(h2, h3);
                    verticalLayout.add(new HorizontalLayout(new NativeLabel("Click on share"), new Svg(ShareIcon.SVG)));
                    verticalLayout.add(new HorizontalLayout(new NativeLabel("Add to Home screen"), LumoIcon.PLUS.create()));
                    add(verticalLayout);
                } else {
                    add("This App works only on mobile device.");
                }
            });
        }
    }
}
