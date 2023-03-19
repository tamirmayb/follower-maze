package com.follower.maze.event.events;

import com.follower.maze.MyLogger;
import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Broadcast extends Event {

    private final int seqId;
    private final String event;

    public Broadcast(Integer seqId, String event) {
        super(seqId);
        this.seqId = seqId;
        this.event = event;
    }

    @Override
    public void sendEventToUsers(Map<Integer, User> users) throws IOException {
        for (User user : users.values()) {

            MyLogger.log(this, "sending " + event + " to users");

            if (user.receiveEvent(event)) {
                MyLogger.log(this, "Got an error while sending event to " + user.getUserNumber() + " which will be removed from users list");
                users.remove(user.getUserNumber());
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Broadcast broadcast = (Broadcast) o;

        if (seqId != broadcast.seqId) return false;
        return Objects.equals(event, broadcast.event);
    }

    @Override
    public int hashCode() {
        int result = seqId;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Broadcast {" +
                "seqId=" + seqId +
                ", event='" + event + '\'' +
                '}';
    }
}
