package com.github.dskprt.catnip;

import com.github.dskprt.catnip.event.EventManager;
import com.github.dskprt.catnip.event.events.KeyboardEvent;
import com.github.dskprt.catnip.event.events.PlayerTickEvent;
import com.github.dskprt.catnip.event.events.Render2DEvent;
import com.github.dskprt.catnip.main.Main;

import com.github.dskprt.catnip.module.Module;
import com.github.dskprt.catnip.module.ModuleManager;
import com.github.dskprt.catnip.settings.Settings;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.controllers.StartupController;
import com.github.dskprt.catnip.utils.Utils;
import com.github.dskprt.catnip.utils.ZipUtils;
import javafx.application.Platform;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;

public class Catnip {

    public static final File CONFIG_DIR = new File(System.getProperty("user.home"), ".catnip");
    public static final File SETTINGS_DIR = new File(CONFIG_DIR, "settings");

    private static Catnip instance;

    private final EventManager eventManager;
    private final ModuleManager moduleManager;

    public Catnip() {
        instance = this;

        if(!CONFIG_DIR.exists()) CONFIG_DIR.mkdir();
        if(!SETTINGS_DIR.exists()) SETTINGS_DIR.mkdir();

        JfxUI.show();

        StartupController controller = JfxUI.getController();
        controller.setStages(5);
        controller.setStage(2);

        eventManager = new EventManager();
        moduleManager = new ModuleManager();

        Platform.runLater(() -> {
            controller.incrementStage();
            controller.loadingInfo.setText("Registering events");
        });

        eventManager.register(Render2DEvent.class, e -> {
            Render2DEvent event = (Render2DEvent) e;

            moduleManager.getModules().stream()
                    .filter(Module::isEnabled)
                    .forEach(m -> m.onRender2D(event.matrices, event.delta));
        });

        eventManager.register(PlayerTickEvent.Post.class, e -> moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .forEach(Module::onUpdate));

        eventManager.register(KeyboardEvent.class, e -> {
            KeyboardEvent event = (KeyboardEvent) e;

            if(event.action != GLFW.GLFW_PRESS) return;

            moduleManager.getModules().stream()
                    .filter(m -> m.getKeybinding() == event.key)
                    .forEach(m -> m.setEnabled(!m.isEnabled()));
        });

        Platform.runLater(() -> {
            controller.incrementStage();
            controller.loadingInfo.setText("Registering shutdown hook");
        });

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        if(MinecraftClient.getInstance().world != null) {
            JfxUI.hide();
        } else {
            JfxUI.setPagePath("/scenes/main.fxml");
        }
    }

    public void shutdown() {
        moduleManager.getModules().forEach(Settings::save);
    }

    public static Catnip getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static class Mod implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

            if(file.isDirectory()) {
                try {
                    ZipUtils.pack(file.getPath(),
                            (file = new File(MinecraftClient.getInstance().runDirectory, "catnip.jar")).toString());
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                ByteBuddyAgent.attach(file, Utils.getCurrentPid(),
                        FabricLoader.getInstance().isDevelopmentEnvironment() ? "debug" : "");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
