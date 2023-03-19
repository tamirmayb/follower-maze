package com.follower.maze.event.processor;

import com.follower.maze.interfaces.ClientProcessor;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.EventType;
import com.follower.maze.event.events.factory.interfaces.EventFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class IncomingEventProcessor implements ClientProcessor {

    private final AtomicBoolean shouldContinueRunning;
    private final Map<EventType, EventFactory> eventTypes;
    private final Queue<Event> events;

    public IncomingEventProcessor(AtomicBoolean shouldContinueRunning,
                                  Queue<Event> events,
                                  Map<EventType, EventFactory> eventTypes) {
        this.shouldContinueRunning = shouldContinueRunning;
        this.events = events;
        this.eventTypes = eventTypes;
    }

    public void processEvents(final Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        while ((line = in.readLine()) != null && shouldContinueRunning.get()) {
            final String[] splitLine = line.split("\\|");
            final String type = splitLine[1];
            final EventFactory eventFactory = eventTypes.get(EventType.valueOfTypeCode(type));
            final Event event = eventFactory.createEvent(splitLine, line);
            events.add(event);
        }
    }
}
