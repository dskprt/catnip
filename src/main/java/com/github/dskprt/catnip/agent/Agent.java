package com.github.dskprt.catnip.agent;

import com.github.dskprt.catnip.agent.transformer.transformers.InGameHudTransformer;
import com.github.dskprt.catnip.agent.transformer.transformers.MinecraftClientTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

public class Agent {

    public static void premain(String arg, Instrumentation inst) {
        new Thread(() -> load(inst)).start();
    }

    public static void agentmain(String arg, Instrumentation inst) {
        new Thread(() -> load(inst)).start();
    }

    public static void load(Instrumentation inst) {
        for(Class<?> cls : inst.getAllLoadedClasses()) {
            if(cls.getName().startsWith("net.minecraft.")) {
                Class<?> cl;

                try {
                    Class<?> knotCl = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
                    cl = (knotCl.cast(cls.getClassLoader())).getClass();
                } catch(ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    Method addURL = cl.getDeclaredMethod("addURL", URL.class);
                    if(!addURL.isAccessible()) addURL.setAccessible(true);

                    addURL.invoke(cls.getClassLoader(), Agent.class.getProtectionDomain().getCodeSource().getLocation());
                } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    return;
                }

                break;
            }
        }

        for(Class<?> cls : inst.getAllLoadedClasses()) {
            if(cls.getName().startsWith("net.minecraft.")) {
                switch(cls.getName()) {
                    case "net.minecraft.client.MinecraftClient":
                        transform(new MinecraftClientTransformer(), cls, inst);
                        break;
                    case "net.minecraft.client.gui.hud.InGameHud":
                        transform(new InGameHudTransformer(), cls, inst);
                        break;
                }
            }
        }
    }

    private static void transform(ClassFileTransformer transformer, Class<?> cls, Instrumentation inst) {
        inst.addTransformer(transformer, true);

        try {
            inst.retransformClasses(cls);
        } catch(UnmodifiableClassException e) {
            e.printStackTrace();
        }

        inst.removeTransformer(transformer);
    }
}
