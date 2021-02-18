package com.github.dskprt.catnip.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.function.Consumer;

public class EventManager {

    private final Multimap<Class<? extends Event>, Consumer<Event>> LISTENERS;

    public EventManager() {
        LISTENERS = ArrayListMultimap.create();
    }

    public void register(Class<? extends Event> cls, Consumer<Event> consumer) {
        LISTENERS.put(cls, consumer);
    }

    public void fire(Event e) {
        LISTENERS.forEach((cls, consumer) -> {
            if(e.getClass() == cls) {
                consumer.accept(e);
            }
        });
    }

    public void clear() {
        LISTENERS.clear();
    }
}
