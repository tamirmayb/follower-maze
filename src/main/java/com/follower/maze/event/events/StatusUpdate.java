package com.follower.maze.event.events;

import com.follower.maze.MyLogger;
import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class StatusUpdate extends Event {

    private final int seqId;
    private final int fromUserId;
    private final String event;

    public StatusUpdate(int seqId, String event, int fromUserId) {
        super(seqId);
        this.seqId = seqId;
        this.fromUserId = fromUserId;
        this.event = event;
    }

    @Override
    public void sendEventToUsers(Map<Integer, User> users) throws IOException {
        User newUser = users.get(fromUserId);

        MyLogger.log(this, "sending " + event + " to " + newUser);
        newUser.notifyFollowers(event);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusUpdate that = (StatusUpdate) o;

        if (fromUserId != that.fromUserId) return false;
        if (seqId != that.seqId) return false;
        return Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        int result = seqId;
        result = 31 * result + fromUserId;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StatusUpdate {" +
                "seqId=" + seqId +
                ", fromUserId=" + fromUserId +
                ", event='" + event + '\'' +
                '}';
    }
}
