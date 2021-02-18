package com.github.dskprt.catnip.agent.transformer.transformers;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public class KeyboardTransformer extends JavassistTransformer {

    public KeyboardTransformer() {
        super("net.minecraft.client.Keyboard");
    }

    @Override
    public CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception {
        CtMethod m = cls.getDeclaredMethod("onKey");
        m.insertBefore("Catnip.getInstance().getEventManager().fire(new KeyboardEvent($2, $4, $5));");

        return cls;
    }
}
