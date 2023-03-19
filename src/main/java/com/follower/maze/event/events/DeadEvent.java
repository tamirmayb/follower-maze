package com.follower.maze.event.events;

import com.follower.maze.users.User;

import java.util.Map;

public class DeadEvent extends Event {

    private static final DeadEvent DEAD_EVENT = new DeadEvent();

    private DeadEvent() {
        super(0);
    }

    public static DeadEvent deadEvent(String[] values) {
        handleDeadEvent(values);
        return DEAD_EVENT;
    }

    @Override
    public void sendEventToUsers(Map<Integer, User> users) {
    }

    @Override
    public String toString() {
        return "DeadEvent{}";
    }

    private static void handleDeadEvent(String[] values) {
        // todo add code which handles dead event data
    }

}
