package com.github.dskprt.catnip.module.modules;

import com.github.dskprt.catnip.module.Module;
import com.github.dskprt.catnip.settings.annotations.*;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@Module.Metadata(id = "test", name = "Test", keybinding = GLFW.GLFW_KEY_U)
public class Test extends Module {

    @BooleanSetting(id = "boolTest", name = "Boolean Test")
    public boolean bTest = false;

    @IntegerSetting(id = "intTest", name = "Integer Test")
    private int iTest = 2;

    @ComboSetting(id = "comboTest", name = "Combo Test", values = { "one", "two", "three", "four" })
    public static String cTest = "three";

    @FloatSetting(id = "floatTest", name = "Float Test", max = 5f)
    public static float fTest = 4.2f;

    @ColorSetting(id = "colorTest", name = "Color Test")
    private static Color clrTest = Color.BLUE;

    @Override
    public void onRender2D(MatrixStack matrixStack, float delta) {
        mc.textRenderer.draw(matrixStack, "Hello!", 5, 5, -1);
    }

    @Override
    public void onEnabled() {
        System.out.println("bTest" + bTest);
        System.out.println("iTest" + iTest);
        System.out.println("cTest" + cTest);
        System.out.println("fTest" + fTest);
        System.out.println("clrTest" + clrTest.toString());
    }
}
