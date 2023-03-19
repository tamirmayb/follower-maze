package com.follower.maze.event.events;

import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;

public abstract class Event implements Comparable<Event> {
    private final int seqId;

    protected Event(int seqId) {
        this.seqId = seqId;
    }

    public int getSeqId() {
        return seqId;
    }

    public abstract void sendEventToUsers(Map<Integer, User> users) throws IOException;

    @Override
    public int compareTo(Event o) {
        return Integer.compare(this.getSeqId(), o.getSeqId());
    }
}
