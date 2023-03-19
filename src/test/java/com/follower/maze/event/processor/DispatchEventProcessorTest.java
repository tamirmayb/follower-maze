package com.follower.maze.event.processor;

import com.follower.maze.event.events.Event;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DispatchEventProcessorTest {

    private final PriorityBlockingQueue<Event> readyToProcessEvents = new PriorityBlockingQueue<>(100);
    private final PriorityBlockingQueue<Event> dispatchEvents = new PriorityBlockingQueue<>(100);
    private final AtomicBoolean shouldContinueRunning = new AtomicBoolean(true);
    private final OutgoingEventProcessor outgoingEventProcessor = new OutgoingEventProcessor(shouldContinueRunning, readyToProcessEvents, dispatchEvents);
    private final Event event = mock(Event.class);

    @Test
    public void testSendEventWhenLowerThanCurrentSeqId() throws Exception {
        readyToProcessEvents.add(event);

        runSendEventProcessor();

        assertEquals(event, dispatchEvents.poll());
    }

    @Test
    public void testSendEventContinueRunningStopsLoop() throws Exception {
        readyToProcessEvents.add(event);
        shouldContinueRunning.set(false);

        runSendEventProcessor();

        assertNull(dispatchEvents.poll());
    }

    @Test
    public void testSendEvent() throws Exception {
        readyToProcessEvents.add(event);
        when(event.getSeqId()).thenReturn(100);

        runSendEventProcessor();

        assertNull(dispatchEvents.poll());
    }

    private void runSendEventProcessor() throws InterruptedException, java.util.concurrent.ExecutionException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Future<?> submit = executorService.submit(outgoingEventProcessor);
        try {
            submit.get(100, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e1) {
            // Do nothing
        }
        shouldContinueRunning.set(false);
    }

}