package com.github.dskprt.catnip.event.events;

import com.github.dskprt.catnip.event.Event;

public class KeyboardEvent extends Event {

    public int key;
    public int action;
    public int modifiers;

    public KeyboardEvent(int key, int action, int modifiers) {
        this.cancellable = true;

        this.key = key;
        this.action = action;
        this.modifiers = modifiers;
    }
}
