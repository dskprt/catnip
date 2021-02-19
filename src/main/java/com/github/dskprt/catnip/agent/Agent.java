package com.github.dskprt.catnip.agent;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import com.github.dskprt.catnip.agent.transformer.transformers.ClientPlayerEntityTransformer;
import com.github.dskprt.catnip.agent.transformer.transformers.InGameHudTransformer;
import com.github.dskprt.catnip.agent.transformer.transformers.KeyboardTransformer;
import com.github.dskprt.catnip.agent.transformer.transformers.MinecraftClientTransformer;
import com.github.dskprt.catnip.ui.JfxUI;
import com.github.dskprt.catnip.ui.controllers.StartupController;
import javafx.application.Platform;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Agent {

    private static final Map<String, JavassistTransformer> transformers = new HashMap<String, JavassistTransformer>() {{
        put("net.minecraft.client.MinecraftClient", new MinecraftClientTransformer());
        put("net.minecraft.client.gui.hud.InGameHud", new InGameHudTransformer());
        put("net.minecraft.client.Keyboard", new KeyboardTransformer());
        put("net.minecraft.client.network.ClientPlayerEntity", new ClientPlayerEntityTransformer());
    }};

    public static void premain(String arg, Instrumentation inst) {
        load(inst);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        load(inst);
    }

    // TODO display errors on the javafx ui
    public static void load(Instrumentation inst) {
        JfxUI.show();

        StartupController controller = JfxUI.getController();
        controller.setStages(5);
        controller.incrementStage();
        controller.loadingInfo.setText("Adding JAR to the Fabric class loader");

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

        controller.incrementStage();

        for(Map.Entry<String, JavassistTransformer> entry : transformers.entrySet()) {
            controller.loadingInfo.setText("Transforming " + entry.getKey() + "...");
            transform(entry.getValue(), getClass(o, m, entry.getKey()), inst);
        }

        JfxUI.hide();
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
