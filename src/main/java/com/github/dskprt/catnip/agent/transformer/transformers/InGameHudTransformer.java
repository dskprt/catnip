package com.github.dskprt.catnip.agent.transformer.transformers;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public class InGameHudTransformer extends JavassistTransformer {

    public InGameHudTransformer() {
        super("net.minecraft.client.gui.hud.InGameHud");
    }

    @Override
    public CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception {
        CtMethod m = cls.getDeclaredMethod("render");
        m.insertAfter("{ Catnip.getInstance().eventManager.fire(new Render2DEvent($1, $2)); }");
        return cls;
    }
}
