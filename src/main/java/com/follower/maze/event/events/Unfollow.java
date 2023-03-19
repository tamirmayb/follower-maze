package com.follower.maze.event.events;

import com.follower.maze.MyLogger;
import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Unfollow extends Event {

    private final int seqId;
    private final int fromUserId;
    private final int toUserId;

    private final String event;

    public Unfollow(int seqId, String event, int fromUserId, int toUserId) {
        super(seqId);
        this.seqId = seqId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.event = event;
    }

    @Override
    public int getSeqId() {
        return seqId;
    }

    @Override
    public void sendEventToUsers(Map<Integer, User> users) throws IOException {
        User toNewUser = users.get(toUserId);
        User fromNewUser = users.get(fromUserId);

        MyLogger.log(this, "sending " + event + " to " + toNewUser);

        toNewUser.removeFollower(fromNewUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unfollow unfollow = (Unfollow) o;

        if (fromUserId != unfollow.fromUserId) return false;
        if (seqId != unfollow.seqId) return false;
        if (toUserId != unfollow.toUserId) return false;
        return Objects.equals(event, unfollow.event);
    }

    @Override
    public int hashCode() {
        int result = seqId;
        result = 31 * result + fromUserId;
        result = 31 * result + toUserId;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Unfollow {" +
                "seqId=" + seqId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", event='" + event + '\'' +
                '}';
    }
}
