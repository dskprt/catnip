package com.github.dskprt.catnip.agent.transformer.transformers;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public class MinecraftClientTransformer extends JavassistTransformer {

    public MinecraftClientTransformer() {
        super("net.minecraft.client.MinecraftClient");
    }

    // TODO find a better solution
    @Override
    public CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception {
        CtMethod m = cls.getDeclaredMethod("tick");
        m.insertBefore("{ if(Catnip.getInstance() == null) new Catnip(); }");

        return cls;
    }
}
