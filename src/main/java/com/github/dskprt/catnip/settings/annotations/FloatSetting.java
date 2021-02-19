package com.github.dskprt.catnip.settings.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FloatSetting {

    String id();
    String name();
    float min() default 0f;
    float max() default 10f;
    int decimal() default 2;
}
