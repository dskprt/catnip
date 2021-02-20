package com.github.dskprt.catnip.ui.controllers;

import com.github.dskprt.catnip.ui.JfxUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainController {

    @FXML
    public Button shutdownBtn;

    @FXML
    public TabPane tabPane;

    @FXML
    public AnchorPane modulesPane;

    @FXML
    public void initialize() {
        // TODO figure out why is this not working
        BackgroundImage backgroundImage = new BackgroundImage(new Image(this.getClass()
                .getResourceAsStream("/icons/outline_power_settings_new_white_24dp.png")),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(24, 24, false, false, false, false));
        Background background = new Background(backgroundImage);
        shutdownBtn.setBackground(background);
    }

    public void shutdownAction(ActionEvent actionEvent) {
        // TODO uninject
        JfxUI.hide();
    }
}
