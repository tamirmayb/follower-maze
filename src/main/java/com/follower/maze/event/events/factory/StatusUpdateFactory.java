package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.StatusUpdate;
import com.follower.maze.event.events.factory.interfaces.EventFactory;

public class StatusUpdateFactory implements EventFactory {

    @Override
    public Event createEvent(String[] values, String event) {
        try {
            return new StatusUpdate(Integer.parseInt(values[0]), event, Integer.parseInt(values[2]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return DeadEvent.deadEvent(values);
        }
    }
}
