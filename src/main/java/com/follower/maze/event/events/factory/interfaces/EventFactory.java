package com.follower.maze.event.events.factory.interfaces;

import com.follower.maze.event.events.Event;

public interface EventFactory {
    Event createEvent(String[] values, String event);
}
