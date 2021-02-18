package com.github.dskprt.catnip.module.modules;

import com.github.dskprt.catnip.module.Module;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

@Module.Metadata(id = "test", name = "Test", keybinding = GLFW.GLFW_KEY_U)
public class Test extends Module {

    @Override
    public void onRender2D(MatrixStack matrixStack, float delta) {
        mc.textRenderer.draw(matrixStack, "Hello!", 5, 5, -1);
    }
}
