package com.github.dskprt.catnip.module.modules;

import com.github.dskprt.catnip.Catnip;
import com.github.dskprt.catnip.module.Module;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.components.SwitchButton;
import com.github.dskprt.catnip.ui.controllers.MainController;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import org.lwjgl.glfw.GLFW;

@Module.Metadata(id = "gui", name = "GUI", category = Module.Category.RENDER, keybinding = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class GUI extends Module {

    private MainController controller;

    @Override
    public void onEnabled() {
        mc.openPauseMenu(true);

        Platform.runLater(() -> {
            if(!JfxUI.pagePath.endsWith("main.fxml")) JfxUI.setPagePath("/scenes/main.fxml");

            controller = JfxUI.getController();

            if(controller.tabPane.getSelectionModel().getSelectedIndex() != 1) {
                controller.tabPane.getSelectionModel().select(1);
            }

            controller.modulesPane.getChildren().clear();

            final int switchWidth = 30;
            final int switchHeight = 15;

            int lastY = 0;

            for(Category category : Category.values()) {
                Module[] modules = Catnip.getInstance().getModuleManager().getModulesInCategory(category);

                if(modules.length == 0) continue;

                // create and add category name label and separator
                Label categoryName = new Label(category.toString());
                categoryName.setPrefWidth(590);
                categoryName.setPrefHeight(47);
                categoryName.setLayoutY(lastY == 0 ? 10 : lastY + 30);
                categoryName.setTextFill(Color.WHITE);
                categoryName.setStyle("-fx-font-size: 32px; -fx-padding: 0 0 0 10;");

                Separator separator = new Separator(Orientation.HORIZONTAL);
                separator.setLayoutX(10);
                separator.setLayoutY(categoryName.getLayoutY() + 50);
                separator.setPrefWidth(570);

                controller.modulesPane.getChildren().add(categoryName);
                controller.modulesPane.getChildren().add(separator);

                int y = (int) separator.getLayoutY() + 10;
                int column = 0;

                for(Module module : modules) {
                    // create and add module name label
                    Label moduleName = new Label(module.getName());
                    moduleName.setStyle("-fx-font-size: 16px;");
                    moduleName.setTextFill(Color.WHITE);

                    controller.modulesPane.getChildren().add(moduleName);
                    moduleName.applyCss();

                    double w = moduleName.prefWidth(-1);
                    double h = moduleName.prefHeight(-1);

                    moduleName.setLayoutX(column == 0 ? 10 : (column == 1
                            ? (controller.modulesPane.getWidth() / 2) - ((w + 10 + switchWidth) / 2)
                            : controller.modulesPane.getWidth() - switchWidth - 10 - w - 10));
                    moduleName.setLayoutY(y);

                    switch(column) {
                        case 0:
                        case 1:
                            column++;
                            break;
                        case 2:
                            column = 0;
                            y += 25;
                            break;
                    }

                    // create and add switch button
                    SwitchButton toggle = new SwitchButton(module);
                    toggle.setLayoutX(moduleName.getLayoutX() + w + 10);
                    toggle.setLayoutY(moduleName.getLayoutY() + 5);

                    controller.modulesPane.getChildren().add(toggle);

                    lastY = y;
                }
            }

            JfxUI.stage.setOnCloseRequest(e -> {
                JfxUI.hide();
                this.setEnabled(false);
            });

            JfxUI.scene.setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.ESCAPE) {
                    JfxUI.hide();
                    this.setEnabled(false);
                }
            });

            if(!JfxUI.stage.isShowing()) JfxUI.show();
            JfxUI.stage.requestFocus();
        });
    }

    @Override
    public void onDisabled() {
        // TODO unpause the game
    }
}
