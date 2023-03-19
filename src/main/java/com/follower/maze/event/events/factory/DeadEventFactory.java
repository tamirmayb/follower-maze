package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.factory.interfaces.EventFactory;

public class DeadEventFactory implements EventFactory {

    @Override
    public Event createEvent(String[] values, String event) {
        System.out.println("found dead");
        return DeadEvent.deadEvent(values);
    }
}
