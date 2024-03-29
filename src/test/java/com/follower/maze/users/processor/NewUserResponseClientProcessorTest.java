package com.follower.maze.users.processor;

import com.follower.maze.event.events.Event;
import com.follower.maze.users.User;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NewUserResponseClientProcessorTest {

    private final ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap<>(1);
    private final ArrayBlockingQueue<Event> dispatchedEvents = new ArrayBlockingQueue<>(100);
    private final AtomicBoolean shouldContinueRunning = new AtomicBoolean(true);
    private final UserResponseClientProcessor userResponseClientProcessor = new UserResponseClientProcessor(shouldContinueRunning, users, dispatchedEvents);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Event eventMock = mock(Event.class);

    @Test
    public void testProcessAUser() throws Exception {
        dispatchedEvents.add(eventMock);
        runUserProcessor();

        verify(eventMock).sendEventToUsers(users);
    }


    private void runUserProcessor() throws InterruptedException, java.util.concurrent.ExecutionException {
        final Future<?> submit = executorService.submit(userResponseClientProcessor);
        try {
            submit.get(10, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Do nothing
        }
        shouldContinueRunning.set(false);
    }
}