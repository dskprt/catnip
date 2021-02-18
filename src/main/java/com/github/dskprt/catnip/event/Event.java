package com.github.dskprt.catnip.event;

public class Event {

    protected boolean cancellable = false;
    protected boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        if(!cancellable) throw new IllegalStateException("Event is not cancellable!");

        this.cancelled = cancelled;
    }
}
