package com.follower_maze.entities;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    FOLLOW("F"),
    UNFOLLOW("U"),
    BROADCAST("B"),
    PRIVATE_MSG("P"),
    STATUS_UPDATE("S"),
    UNKNOWN("UNKNOWN");

    public final String label;

    private EventType(String label) {
        this.label = label;
    }

    private static final Map<String, EventType> map = new HashMap<>();
    static {
        for (EventType c : values()) map.put(c.label, c);
    }
    public static EventType of(String name) {
        EventType result = map.get(name);
        if (result == null) {
            throw new IllegalArgumentException("Invalid EventType name: " + name);
        }
        return result;
    }
}
