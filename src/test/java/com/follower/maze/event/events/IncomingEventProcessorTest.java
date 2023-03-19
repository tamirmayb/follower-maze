package com.follower.maze.event.events;

import com.follower.maze.event.events.factory.interfaces.EventFactory;
import com.follower.maze.event.processor.IncomingEventProcessor;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IncomingEventProcessorTest {

    private final AtomicBoolean shouldContinueRunning = new AtomicBoolean(true);
    private final PriorityBlockingQueue<Event> readyToProcessEvents = new PriorityBlockingQueue<>(100);
    private final EventFactory eventFactoryMock = mock(EventFactory.class);
    private final Map eventTypes = mock(Map.class);
    private final Socket socketMock = mock(Socket.class);
    private final Event eventMock = mock(Event.class);
    private final IncomingEventProcessor incomingEventProcessor = new IncomingEventProcessor(shouldContinueRunning, readyToProcessEvents, eventTypes);

    @Test
    public void testContinueRunningStopsLoop() throws Exception {
        createIncomingMessageFrom("123|F|460|550\r\n");
        shouldContinueRunning.set(false);

        incomingEventProcessor.processEvents(socketMock);

        assertNull(readyToProcessEvents.poll());
    }

    private void createIncomingMessageFrom(String input) throws IOException {
        when(eventTypes.get(anyString())).thenReturn(eventFactoryMock);
        when(eventFactoryMock.createEvent(any(String[].class), anyString())).thenReturn(eventMock);
        when(socketMock.getInputStream()).thenReturn(IOUtils.toInputStream(input));
    }
}