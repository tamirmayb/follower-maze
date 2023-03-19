package com.follower.maze.event.events.factory;

import com.follower.maze.event.events.DeadEvent;
import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.PrivateMsg;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class PrivateMsgFactoryTest {
    public static final String TYPE = "P";
    private final EventFactory factory = new PrivateMsgFactory();

    @Test
    public void test() throws Exception {
        final String eventString = "123|" + TYPE + "|34|56";
        String[] values = eventString.split("\\|");

        final Event event = factory.createEvent(values, eventString);

        assertEquals(new PrivateMsg(123, eventString, 34, 56), event);
    }

    @Test
    public void testInvalidSequenceEvent() {
        final String eventString = "test|" + TYPE + "|34|56";
        String[] values = eventString.split("\\|");

        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidToEvent() {
        final String eventString = "123|" + TYPE + "|test|50";
        String[] values = eventString.split("\\|");

        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }

    @Test
    public void testInvalidNumberOfAttributes() {
        final String eventString = "123|" + TYPE;
        String[] values = eventString.split("\\|");

        final DeadEvent expectedDeadEvent = DeadEvent.deadEvent(values);
        final Event event = factory.createEvent(values, eventString);

        assertEquals(expectedDeadEvent, event);
    }
}