package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.Broadcast;
import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DeadFactoryTest {

    private static final String TYPE = "D";
    private final EventFactory factory = new DeadEventFactory();

    @Test
    public void testValidDeadEvent() {

        final String eventString = "666|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(DeadEvent.deadEvent(values), event);
    }

}