package com.github.dskprt.catnip.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JfxUI extends Application {

    private static String pagePath;
    private static FXMLLoader loader;

    private static Stage stage;
    public static Scene scene;

    @Override
    public void init() throws Exception {
        loader = new FXMLLoader(this.getClass().getResource("/scenes/startup.fxml"));
        scene = new Scene(loader.load());
    }

    @Override
    public void start(Stage stage) throws Exception {
        JfxUI.stage = stage;

        stage.setTitle("catnip");
        stage.setResizable(false);
        stage.setScene(scene);
    }

    public synchronized static void show() {
        if(stage == null) {
            new Thread(() -> Application.launch(JfxUI.class), "JavaFX Startup Thread").start();

            while(stage == null) {
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Platform.runLater(() -> {
            stage.setScene(scene);
            stage.show();
        });
    }

    public synchronized static void hide() {
        if(stage == null) {
            new Thread(() -> Application.launch(JfxUI.class), "JavaFX Startup Thread").start();

            while(stage == null) {
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Platform.runLater(() -> stage.hide());
    }

    public static <T> T getController() {
        return loader.getController();
    }

    public static void setPagePath(String fxmlPath) {
        pagePath = fxmlPath;

        Platform.runLater(() -> {
            loader = new FXMLLoader(JfxUI.class.getResource(pagePath));

            try {
                scene = new Scene(loader.load());
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }
}
