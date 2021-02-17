package com.github.dskprt.catnip;

import com.github.dskprt.catnip.main.Main;
import com.github.dskprt.catnip.utils.Utils;
import com.github.dskprt.catnip.utils.ZipUtils;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Catnip {

    public static class Mod implements ClientModInitializer {

        @Override
        public void onInitializeClient() {
            File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());

            Toolkit.getDefaultToolkit().beep();

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
                //JOptionPane.showMessageDialog(frame, "Failed to attach agent", "catnip", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
