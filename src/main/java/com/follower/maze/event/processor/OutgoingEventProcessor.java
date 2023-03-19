package com.follower.maze.event.processor;

import com.follower.maze.interfaces.MyAbstractServer;
import com.follower.maze.event.events.Event;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class OutgoingEventProcessor extends MyAbstractServer {

    private final AtomicBoolean shouldContinueRunning;
    private int currentSeqId = 1;

    private final Queue<Event> events;
    private final Queue<Event> dispatchEvents;

    public OutgoingEventProcessor(
            AtomicBoolean shouldContinueRunning,
            Queue<Event> events,
            Queue<Event> dispatchEvents) {

        this.shouldContinueRunning = shouldContinueRunning;
        this.events = events;
        this.dispatchEvents = dispatchEvents;
    }

    @Override
    public void run() {
        while (shouldContinueRunning.get()) {
            final Event peek = events.peek();
            if (peek != null && peek.getSeqId() <= currentSeqId) {
                final Event take = events.poll();
                if (dispatchEvents.add(take)) {
                    currentSeqId++;
                }
            }
        }
    }

    @Override
    public void shutDown() throws IOException {
        // Nothing to shutdown
    }

}
