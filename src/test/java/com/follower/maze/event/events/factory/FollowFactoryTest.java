package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.Follow;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class FollowFactoryTest {

    public static final String TYPE = "F";
    private final EventFactory factory = new FollowFactory();
    private final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(new String[] {});

    @Test
    public void testValidEvent() {
        final String eventString = "666|" + TYPE + "|3|40";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(new Follow(666, eventString, 3, 40), event);
    }

    @Test
    public void testInvalidSequenceEvent() {
        final String eventString = "test|" + TYPE + "|3|40";
        String[] values = eventString.split("\\|");
        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidToEvent() throws Exception {
        final String eventString = "123|" + TYPE + "|test|45";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidNumberOfAttributes() throws Exception {
        final String eventString = "123|" + TYPE;
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }
}