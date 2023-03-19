package com.follower.maze.users.processor;

import com.follower.maze.MyLogger;
import com.follower.maze.interfaces.MyAbstractServer;
import com.follower.maze.event.events.Event;
import com.follower.maze.users.User;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserResponseClientProcessor extends MyAbstractServer {
    private final Map<Integer, User> users;
    private final AtomicBoolean shouldContinueRunning;
    private final Queue<Event> events;

    public UserResponseClientProcessor(AtomicBoolean shouldContinueRunning, Map<Integer, User> users, Queue<Event> events) {
        this.users = users;
        this.events = events;
        this.shouldContinueRunning = shouldContinueRunning;
    }

    @Override
    public void run() {
        while (shouldContinueRunning.get()) {
            final Event event = events.poll();
            if (event != null) {
                try {
                    event.sendEventToUsers(users);
                    MyLogger.log(this, "sending " + event + " to all users ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void shutDown() throws IOException {
        for (User user : users.values()) {
            user.shutDown();
        }
    }
}
