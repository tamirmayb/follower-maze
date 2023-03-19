package com.follower.maze.event.events;

import com.follower.maze.MyLogger;
import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class PrivateMsg extends Event {

    private final int seqId;
    private final int fromUserId;
    private final int toUserId;
    private final String event;

    public PrivateMsg(int seqId, String event, int fromUserId, int toUserId) {
        super(seqId);
        this.seqId = seqId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.event = event;
    }

    @Override
    public void sendEventToUsers(Map<Integer, User> users) throws IOException {
        User toNewUser = users.get(toUserId);

        MyLogger.log(this, "sending " + event + " to user" + toNewUser);

        if (toNewUser.receiveEvent(event)) {
            MyLogger.log(this, "Got an error while sending to user " + toNewUser.getUserNumber());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrivateMsg that = (PrivateMsg) o;

        if (fromUserId != that.fromUserId) return false;
        if (seqId != that.seqId) return false;
        if (toUserId != that.toUserId) return false;
        return Objects.equals(event, that.event);
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
        return "PrivateMessage{" +
                "seqId=" + seqId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", event='" + event + '\'' +
                '}';
    }
}
