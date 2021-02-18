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
        load(inst);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        load(inst);
    }

    public static void load(Instrumentation inst) {
        Method m = null;
        Object o = null;

        for(Class<?> cls : inst.getAllLoadedClasses()) {
            if(cls.getName().startsWith("net.minecraft.")) {
                Class<?> cl;

                try {
                    Class<?> knotCl = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
                    o = knotCl.cast(cls.getClassLoader());
                    cl = o.getClass();
                } catch(ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                    return;
                }

                try {
                    m = cl.getDeclaredMethod("loadClass", String.class, boolean.class);
                    if(!m.isAccessible()) m.setAccessible(true);

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

        transform(new MinecraftClientTransformer(), getClass(o, m, "net.minecraft.client.MinecraftClient"), inst);
        transform(new InGameHudTransformer(), getClass(o, m, "net.minecraft.client.gui.hud.InGameHud"), inst);
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

    private static Class<?> getClass(Object o, Method loadClass, String name) {
        try {
            return (Class<?>) loadClass.invoke(o, name, false);
        } catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
