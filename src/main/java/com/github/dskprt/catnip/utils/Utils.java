package com.github.dskprt.catnip.utils;

import net.minecraft.client.MinecraftClient;

import java.lang.management.ManagementFactory;

public class Utils {

    //TODO a more universal solution?
    public static String getCurrentPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static int[] centerInMinecraft(int width, int height) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if(mc.getWindow() == null) return new int[] { 0, 0 };

        int x = (mc.getWindow().getWidth() - width) / 2;
        int y = (mc.getWindow().getHeight() - height) / 2;

        x += mc.getWindow().getX();
        y += mc.getWindow().getY();

        return new int[] { x, y };
    }
}
