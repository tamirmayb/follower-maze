package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.Unfollow;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UnfollowFactoryTest {
    public static final String TYPE = "U";
    private final EventFactory factory = new UnfollowFactory();
    private final DeadEvent expectedDeadEvent = DeadEvent.deadEvent();

    @Test
    public void testValidEvent() {
        final String eventString = "123|" + TYPE + "|20|30";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(new Unfollow(123, eventString, 20, 30), event);
    }

    @Test
    public void testInvalidSequenceEvent() {
        final String eventString = "test|" + TYPE + "|20|30";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidToEvent() {
        final String eventString = "123|" + TYPE + "|test|20";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidNumberOfAttributes() {
        final String eventString = "123|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

}