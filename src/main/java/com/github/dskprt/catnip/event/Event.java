package com.github.dskprt.catnip.event;

public class Event {

    protected boolean cancellable = false;
    protected boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
