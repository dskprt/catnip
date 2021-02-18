package com.github.dskprt.catnip.agent.transformer.transformers;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public class MinecraftClientTransformer extends JavassistTransformer {

    public MinecraftClientTransformer() {
        super("net.minecraft.client.MinecraftClient");
    }

    @Override
    public CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception {
        CtMethod m = cls.getDeclaredMethod("run");
        m.insertBefore("new Catnip();");

        return cls;
    }
}
