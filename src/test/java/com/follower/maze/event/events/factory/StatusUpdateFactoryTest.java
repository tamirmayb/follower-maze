package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.StatusUpdate;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StatusUpdateFactoryTest {

    public static final String TYPE = "S";
    private final EventFactory factory = new StatusUpdateFactory();

    @Test
    public void test() throws Exception {
        final String eventString = "123|" + TYPE + "|20";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(new StatusUpdate(123, eventString, 20), event);
    }

    @Test
    public void testInvalidSequenceEvent() {
        final String eventString = "test|" + TYPE + "|20|30";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);
        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidToEvent() {
        final String eventString = "123|" + TYPE + "|test|20";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);
        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidNumberOfAttributes() {
        final String eventString = "123|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);
        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        assertEquals(expectedDeadEvent, event);
    }
}