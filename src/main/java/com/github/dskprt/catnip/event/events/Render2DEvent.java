package com.github.dskprt.catnip.event.events;

import com.github.dskprt.catnip.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render2DEvent extends Event {

    public MatrixStack matrices;
    public float delta;

    public Render2DEvent(MatrixStack matrices, float delta) {
        this.matrices = matrices;
        this.delta = delta;
    }
}
