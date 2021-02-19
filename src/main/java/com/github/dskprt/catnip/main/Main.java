package com.github.dskprt.catnip.main;

import net.bytebuddy.agent.ByteBuddyAgent;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("catnip");
        frame.setUndecorated(true);

        String pid;

        if(System.getProperty("os.name").startsWith("Windows")) {
            try {
                Runtime runtime = Runtime.getRuntime();
                Process proc = runtime.exec("powershell \"(Get-Process | Where-Object { $_.MainWindowTitle -like 'Minecraft*' }).Id\"");

                BufferedReader stdin = new BufferedReader(new
                        InputStreamReader(proc.getInputStream()));

                pid = stdin.readLine();

                if(pid == null) {
                    JOptionPane.showMessageDialog(frame, "Minecraft 1.16.x is not running!", "catnip", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    return;
                }
            } catch(IOException e) {
                e.printStackTrace();

                pid = JOptionPane.showInputDialog(frame, "Minecraft PID", "catnip", JOptionPane.QUESTION_MESSAGE);
            }
        } else {
            pid = JOptionPane.showInputDialog(frame, "Minecraft PID", "catnip", JOptionPane.QUESTION_MESSAGE);
        }

        try {
            ByteBuddyAgent.attach(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()),
                    pid, (args.length > 0) ? (args[0].equals("--debug") ? "debug" : "") : "");
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to attach agent", "catnip", JOptionPane.ERROR_MESSAGE);
        }

        frame.dispose();
    }
}
