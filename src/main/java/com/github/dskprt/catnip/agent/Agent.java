package com.github.dskprt.catnip.agent;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String arg, Instrumentation inst) {
        new Thread(() -> load(inst)).start();
    }

    public static void agentmain(String arg, Instrumentation inst) {
        new Thread(() -> load(inst)).start();
    }

    public static void load(Instrumentation inst) {
        MinecraftClient mc = null;
        TextRenderer tr = null;
        int counter = 0;

        // avoid crash when injecting very early into the game
        while(mc == null) {
            mc = MinecraftClient.getInstance();
        }

        while(tr == null) {
            tr = mc.textRenderer;
        }

        final MinecraftClient m = mc;

        new Thread(() -> {
            m.getWindow().setTitle("ERROR - Class transformation not supported!");

            try {
                Thread.sleep(5000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            m.updateWindowTitle();
        }).start();

//        if(!inst.isRetransformClassesSupported()) {
//            final MinecraftClient m = mc;
//
//            new Thread(() -> {
//                m.getWindow().setTitle("ERROR - Class transformation not supported!");
//
//                try {
//                    Thread.sleep(5000);
//                } catch(InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                m.updateWindowTitle();
//            }).start();
//        }
    }
}
