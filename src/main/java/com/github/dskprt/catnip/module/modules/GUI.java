package com.github.dskprt.catnip.module.modules;

import com.github.dskprt.catnip.Catnip;
import com.github.dskprt.catnip.module.Module;
import com.github.dskprt.catnip.settings.Settings;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.components.SwitchButton;
import com.github.dskprt.catnip.ui.controllers.MainController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

            AtomicBoolean settingsOpen = new AtomicBoolean(false);
            AtomicReference<ObservableList<Node>> oldList = new AtomicReference<>(null);

            //AtomicReference<Rectangle> settingsRect = new AtomicReference<>();
            //AtomicReference<ScrollPane> settingsPane = new AtomicReference<>();

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

                    // TODO fix this or something i dont even know anymore
                    moduleName.setOnMouseClicked(e -> {
                        if(e.getButton() == MouseButton.SECONDARY && !settingsOpen.get()) {
                            ObservableList<Node> list = FXCollections.emptyObservableList();
                            FXCollections.copy(controller.modulesPane.getChildren(), list);
                            oldList.set(list);

                            controller.modulesPane.getChildren().clear();

//                            settingsRect.set(new Rectangle(170, 50, 300, 250));
//                            settingsRect.get().setFill(Color.web("#202020"));
//
//                            settingsPane.set(new ScrollPane());
//                            settingsPane.get().setLayoutX(170);
//                            settingsPane.get().setLayoutY(50);
//                            settingsPane.get().setPrefWidth(250);
//                            settingsPane.get().setPrefHeight(300);
//
//                            AnchorPane pane = new AnchorPane();
//                            pane.setPrefWidth(249);
//                            pane.setPrefHeight(299);
//
//                            settingsPane.get().setContent(pane);
//                            pane.applyCss();
//
                            final AtomicInteger prevY = new AtomicInteger(0);

                            Settings.iterate(module, (setting, val, f) -> {
                                Label settingName = new Label(setting.name());
                                settingName.setStyle("-fx-font-size: 14px;");
                                settingName.setTextFill(Color.WHITE);
                                settingName.setLayoutX(5);
                                settingName.setLayoutY(prevY.get() + 10);

                                controller.modulesPane.getChildren().add(settingName);
                                settingName.applyCss();

                                ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(setting.values()));
                                comboBox.setLayoutX(5);
                                comboBox.setLayoutY(settingName.getLayoutY() + settingName.prefHeight(-1) + 5);
                                comboBox.getSelectionModel().select(val);
                                comboBox.getSelectionModel().selectedItemProperty()
                                        .addListener(((observable, oldValue, newValue) -> {
                                            if(!f.isAccessible()) f.setAccessible(true);
                                            Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                                            try {
                                                f.set(o, newValue);
                                            } catch(IllegalAccessException ex) {
                                                ex.printStackTrace();
                                            }
                                        }));

                                controller.modulesPane.getChildren().add(comboBox);
                                comboBox.applyCss();

                                //prevY.set((int) (comboBox.getLayoutY() + comboBox.prefHeight(-1)));
                                prevY.getAndAdd(50);
                            }, (setting, val, f) -> {
                                Label settingName = new Label(setting.name());
                                settingName.setStyle("-fx-font-size: 14px;");
                                settingName.setTextFill(Color.WHITE);
                                settingName.setLayoutX(5);
                                settingName.setLayoutY(prevY.get() + 10);

                                controller.modulesPane.getChildren().add(settingName);
                                settingName.applyCss();

                                Slider slider = new Slider(setting.min(), setting.max(), val);
                                slider.setLayoutX(5);
                                slider.setLayoutY(settingName.getLayoutY() + settingName.prefHeight(-1) + 5);
                                slider.setBlockIncrement(1);
                                slider.setMajorTickUnit(1);
                                slider.setMinorTickCount(0);
                                slider.setShowTickLabels(true);
                                slider.setSnapToTicks(true);
                                slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    if(!f.isAccessible()) f.setAccessible(true);
                                    Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                                    try {
                                        f.set(o, newValue.intValue());
                                    } catch(IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    }
                                });

                                controller.modulesPane.getChildren().add(slider);
                                slider.applyCss();

                                //prevY.set((int) (slider.getLayoutY() + slider.prefHeight(-1)));
                                prevY.getAndAdd(50);
                            }, (setting, val, f) -> {
                                Label settingName = new Label(setting.name());
                                settingName.setStyle("-fx-font-size: 14px;");
                                settingName.setTextFill(Color.WHITE);
                                settingName.setLayoutX(5);
                                settingName.setLayoutY(prevY.get() + 10);

                                controller.modulesPane.getChildren().add(settingName);
                                settingName.applyCss();

                                Slider slider = new Slider(setting.min(), setting.max(), val);
                                slider.setLayoutX(5);
                                slider.setLayoutY(settingName.getLayoutY() + settingName.prefHeight(-1) + 5);
                                slider.setBlockIncrement(1f / (10 ^ setting.decimal()));
                                slider.setMajorTickUnit(1);
                                slider.setMinorTickCount((10 ^ setting.decimal()) - 1);
                                slider.setShowTickLabels(true);
                                slider.setSnapToTicks(true);
                                slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    if(!f.isAccessible()) f.setAccessible(true);
                                    Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                                    try {
                                        f.set(o, newValue.floatValue());
                                    } catch(IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    }
                                });

                                controller.modulesPane.getChildren().add(slider);
                                slider.applyCss();

                                //prevY.set((int) (slider.getLayoutY() + slider.prefHeight(-1)));
                                prevY.getAndAdd(50);
                            }, (setting, val, f) -> {
                                Label settingName = new Label(setting.name());
                                settingName.setStyle("-fx-font-size: 14px;");
                                settingName.setTextFill(Color.WHITE);
                                settingName.setLayoutX(5);
                                settingName.setLayoutY(prevY.get() + 10);

                                controller.modulesPane.getChildren().add(settingName);
                                settingName.applyCss();

                                CheckBox checkBox = new CheckBox();
                                checkBox.setLayoutX(5);
                                checkBox.setLayoutY(settingName.getLayoutY() + settingName.prefHeight(-1) + 5);
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                    if(!f.isAccessible()) f.setAccessible(true);
                                    Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                                    try {
                                        f.set(o, newValue);
                                    } catch(IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    }
                                });

                                controller.modulesPane.getChildren().add(checkBox);
                                checkBox.applyCss();

                                //prevY.set((int) (checkBox.getLayoutY() + checkBox.prefHeight(-1)));
                                prevY.getAndAdd(50);
                            }, (setting, val, f) -> {
                                Label settingName = new Label(setting.name());
                                settingName.setStyle("-fx-font-size: 14px;");
                                settingName.setTextFill(Color.WHITE);
                                settingName.setLayoutX(5);
                                settingName.setLayoutY(prevY.get() + 10);

                                controller.modulesPane.getChildren().add(settingName);
                                settingName.applyCss();

                                ColorPicker colorPicker = new ColorPicker(Color.rgb(val.getRed(),
                                        val.getGreen(), val.getBlue(), val.getAlpha() / 255f));
                                colorPicker.setLayoutX(5);
                                colorPicker.setLayoutY(settingName.getLayoutY() + settingName.prefHeight(-1) + 5);
                                colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                                    if(!f.isAccessible()) f.setAccessible(true);
                                    Object o = Modifier.isStatic(f.getModifiers()) ? null : module;

                                    try {
                                        f.set(o, new java.awt.Color((float) newValue.getRed(), (float) newValue.getGreen(),
                                                (float) newValue.getBlue(), (float) newValue.getOpacity()));
                                    } catch(IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    }
                                });

                                controller.modulesPane.getChildren().add(colorPicker);
                                colorPicker.applyCss();

                                //prevY.set((int) (colorPicker.getLayoutY() + colorPicker.prefHeight(-1)));
                                prevY.getAndAdd(50);
                            });

//                            controller.modulesPane.getChildren().add(settingsRect.get());
//                            controller.modulesPane.getChildren().add(settingsPane.get());

                            settingsOpen.set(true);
                        }
                    });

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
                    SwitchButton toggle = new SwitchButton(module, settingsOpen);
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
                    if(settingsOpen.get()) {
                        //controller.modulesPane.getChildren().remove(settingsRect.get());
                        //controller.modulesPane.getChildren().remove(settingsPane.get());
                        if(oldList.get() != null) controller.modulesPane.getChildren().setAll(oldList.get());
                        settingsOpen.set(false);
                    } else {
                        JfxUI.hide();
                        this.setEnabled(false);
                    }
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
