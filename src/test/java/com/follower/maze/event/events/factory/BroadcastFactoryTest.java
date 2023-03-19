package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.Broadcast;
import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class BroadcastFactoryTest {

    private static final String TYPE = "B";
    private final EventFactory factory = new BroadcastFactory();

    @Test
    public void testValidEvent() {

        final String eventString = "666|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(new Broadcast(666, eventString), event);
    }

    @Test
    public void testInvalidEvent() {
        final String eventString = "test|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(DeadEvent.deadEvent(), event);
    }


}