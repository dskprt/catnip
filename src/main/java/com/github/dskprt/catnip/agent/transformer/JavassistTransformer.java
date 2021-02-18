package com.github.dskprt.catnip.agent.transformer;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public abstract class JavassistTransformer implements ClassFileTransformer {

    public ClassPool pool;
    public String toTransform;

    public JavassistTransformer(String toTransform) {
        this.pool = ClassPool.getDefault();
        this.toTransform = toTransform;
    }

    @Override
    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) {
        if(!s.replaceAll("/", ".").equals(toTransform)) {
            return null;
        }

        try {
            pool.appendClassPath(new LoaderClassPath(classLoader));

            pool.importPackage("com.github.dskprt.catnip");
            pool.importPackage("com.github.dskprt.catnip.event.events");

            CtClass ctClass = pool.makeClass(new ByteArrayInputStream(bytes));

            if(!ctClass.isFrozen()) {
                try {
                    return transform(classLoader, s, aClass, ctClass).toBytecode();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract CtClass transform(ClassLoader classLoader, String name, Class<?> _cls, CtClass cls) throws Exception;
}
