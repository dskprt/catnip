package com.github.dskprt.catnip.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Module {

    private final String id = this.getClass().getAnnotation(Metadata.class).id();
    private final String name = this.getClass().getAnnotation(Metadata.class).name();
    private final String description = this.getClass().getAnnotation(Metadata.class).description();
    private final Category category = this.getClass().getAnnotation(Metadata.class).category();
    private int keybinding = this.getClass().getAnnotation(Metadata.class).keybinding();
    private boolean enabled = this.getClass().getAnnotation(Metadata.class).enabled();

    protected MinecraftClient mc = MinecraftClient.getInstance();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeybinding() {
        return keybinding;
    }

    public void setKeybinding(int keybinding) {
        this.keybinding = keybinding;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if(enabled) {
            onEnabled();
        } else {
            onDisabled();
        }

        this.enabled = enabled;
    }

    public void onEnabled() { }
    public void onDisabled() { }
    public void onRender2D(MatrixStack matrixStack, float delta) { }
    public void onUpdate() { }

    public enum Category {

        COMBAT("Combat"),
        MOVEMENT("Movement"),
        RENDER("Render"),
        PLAYER("Player"),
        WORLD("World"),
        MISC("Miscellaneous");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Metadata {

        String id();
        String name();
        String description() default "";
        Category category() default Category.MISC;
        int keybinding() default GLFW.GLFW_KEY_UNKNOWN;
        boolean enabled() default false;
    }
}
