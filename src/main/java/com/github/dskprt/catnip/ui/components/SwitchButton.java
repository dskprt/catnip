package com.github.dskprt.catnip.ui.components;

import com.github.dskprt.catnip.module.Module;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/*
https://stackoverflow.com/a/57290206
 */
public class SwitchButton extends StackPane {

    private final Rectangle back = new Rectangle(30, 10, Color.RED);
    private final Button button = new Button();

    private final String buttonStyleOff = "-fx-background-color: WHITE;";
    private final String buttonStyleOn = "-fx-background-color: #00893d;";

    private boolean state;

    public SwitchButton() {
        getChildren().addAll(back, button);
        setMinSize(30, 15);

        back.maxWidth(30);
        back.minWidth(30);
        back.maxHeight(10);
        back.minHeight(10);
        back.setArcHeight(back.getHeight());
        back.setArcWidth(back.getHeight());
        back.setFill(Color.valueOf("#ced5da"));

        double r = 2.0;

        button.setShape(new Circle(r));
        setAlignment(button, Pos.CENTER_LEFT);
        button.setMaxSize(15, 15);
        button.setMinSize(15, 15);
        button.setStyle(buttonStyleOff);

        EventHandler<Event> click = e -> setState(!getState());

        button.setFocusTraversable(false);
        setClickEvent(click);
    }

    // TODO cleaner and more modular way for multiple click events
    public SwitchButton(Module module) {
        getChildren().addAll(back, button);
        setMinSize(30, 15);

        back.maxWidth(30);
        back.minWidth(30);
        back.maxHeight(10);
        back.minHeight(10);
        back.setArcHeight(back.getHeight());
        back.setArcWidth(back.getHeight());
        back.setFill(module.isEnabled() ? Color.valueOf("#80C49E") : Color.valueOf("#ced5da"));

        double r = 2.0;

        button.setShape(new Circle(r));
        setAlignment(button, module.isEnabled() ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        button.setMaxSize(15, 15);
        button.setMinSize(15, 15);
        button.setStyle(module.isEnabled() ? buttonStyleOn : buttonStyleOff);

        state = module.isEnabled();

        button.setFocusTraversable(false);
        setClickEvent(e -> {
            setState(!getState());
            module.setEnabled(getState());
        });
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        if(state) {
            button.setStyle(buttonStyleOn);
            back.setFill(Color.valueOf("#80C49E"));
            setAlignment(button, Pos.CENTER_RIGHT);
            this.state = true;
        } else {
            button.setStyle(buttonStyleOff);
            back.setFill(Color.valueOf("#ced5da"));
            setAlignment(button, Pos.CENTER_LEFT);
            this.state = false;
        }
    }

    public void setClickEvent(EventHandler<? super MouseEvent> handler) {
        setOnMouseClicked(handler);
        button.setOnMouseClicked(handler);
    }
}
