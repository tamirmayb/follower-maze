package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.Broadcast;
import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.factory.interfaces.EventFactory;

public class BroadcastFactory implements EventFactory {

    @Override
    public Event createEvent(String[] values, String event) {
        try {
            return new Broadcast(Integer.parseInt(values[0]), event);
        } catch (NumberFormatException e) {
            return DeadEvent.deadEvent();
        }
    }
}
