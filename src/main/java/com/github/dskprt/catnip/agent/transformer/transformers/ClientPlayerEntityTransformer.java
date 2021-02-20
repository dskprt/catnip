package com.github.dskprt.catnip.agent.transformer.transformers;

import com.github.dskprt.catnip.agent.transformer.JavassistTransformer;
import javassist.CtClass;
import javassist.CtMethod;

public class ClientPlayerEntityTransformer extends JavassistTransformer {

    public ClientPlayerEntityTransformer() {
        super("net.minecraft.client.network.ClientPlayerEntity");
    }

    @Override
    public CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception {
        CtMethod m = cls.getDeclaredMethod("tick");
        m.insertBefore("Catnip.getInstance().getEventManager().fire(new com.github.dskprt.catnip.event.events.PlayerTickEvent.Pre());");
        m.insertAfter("Catnip.getInstance().getEventManager().fire(new com.github.dskprt.catnip.event.events.PlayerTickEvent.Post());");

        return cls;
    }
}
