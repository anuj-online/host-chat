package com.example.application.views;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.select.Select;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ThemingView extends Dialog {

    //this code worked on UI, not working on Layout. Need a good place to integrate this
    //the idea is to store the color in local storage, read on load, if present update the color
    public ThemingView() {
        WebStorage.getItem("bg", bgcolor -> {
            AtomicReference<String> bg = new AtomicReference<>();
            List<String> colors = List.of("red", "yellow", "green");
            Select<String> stringSelect = new Select<>();
            stringSelect.setLabel("Select background color");
            stringSelect.setItems(colors);
            stringSelect.addValueChangeListener(v -> {
                bg.set(stringSelect.getValue());
                WebStorage.setItem("bg", stringSelect.getValue());
            });
            add(stringSelect);
            if (bgcolor != null) {
                bg.set(bgcolor);
            }
            this.getParent().ifPresent(component -> component.getStyle().set("background", bg.get()));
        });
    }
}
