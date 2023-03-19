package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.Unfollow;
import com.follower.maze.event.events.factory.interfaces.EventFactory;

public class UnfollowFactory implements EventFactory {

    @Override
    public Event createEvent(String[] values, String event) {
        try {
            return new Unfollow(Integer.parseInt(values[0]), event, Integer.parseInt(values[2]), Integer.parseInt(values[3]));
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return DeadEvent.deadEvent();
        }
    }
}
