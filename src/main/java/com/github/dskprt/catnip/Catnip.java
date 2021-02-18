package com.github.dskprt.catnip;

import com.github.dskprt.catnip.event.EventManager;
import com.github.dskprt.catnip.event.events.Render2DEvent;
import com.github.dskprt.catnip.main.Main;

import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.controllers.StartupController;
import com.github.dskprt.catnip.utils.Utils;
import com.github.dskprt.catnip.utils.ZipUtils;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.IOException;

public class Catnip {

    private static Catnip instance;

    private final EventManager eventManager;

    public Catnip() {
        instance = this;

        eventManager = new EventManager();
        eventManager.register(Render2DEvent.class, (e) -> {
            Render2DEvent event = (Render2DEvent) e;

            MinecraftClient.getInstance().textRenderer.draw(event.matrices, "Hello!", 5, 5, 0xffffff);
        });
    }

    public static Catnip getInstance() {
        if(instance == null) throw new IllegalStateException("Not yet initialized!");
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
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
