package com.follower.maze;

import com.follower.maze.event.events.Event;
import com.follower.maze.event.events.EventType;
import com.follower.maze.event.events.factory.BroadcastFactory;
import com.follower.maze.event.events.factory.interfaces.EventFactory;
import com.follower.maze.event.events.factory.FollowFactory;
import com.follower.maze.event.events.factory.PrivateMsgFactory;
import com.follower.maze.event.events.factory.StatusUpdateFactory;
import com.follower.maze.event.events.factory.UnfollowFactory;
import com.follower.maze.event.processor.OutgoingEventProcessor;
import com.follower.maze.event.processor.IncomingEventProcessor;
import com.follower.maze.interfaces.MyAbstractServer;
import com.follower.maze.users.MyUsersHashMap;
import com.follower.maze.users.User;
import com.follower.maze.users.processor.IncomingUserClientProcessor;
import com.follower.maze.users.processor.UserResponseClientProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    private static final int SERVER_SOCKET_PORT = 9090;
    private static final int USER_SERVER_SOCKET_PORT = 9099;
    private static final int SERVER_TIMEOUT_IN_MS = 1000;

    private static final Map<Integer, User> users = new MyUsersHashMap(new ConcurrentHashMap<Integer, User>(200));
    private static final AtomicBoolean shouldKeepAlive = new AtomicBoolean(true);
    private static final Thread mainThread = Thread.currentThread();

    private static final Thread shutdownHookThread = new Thread() {
        public void run() {
            shouldKeepAlive.set(false);
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    };

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(shutdownHookThread);

        final ConcurrentHashMap<EventType, EventFactory> eventTypes = new ConcurrentHashMap<EventType, EventFactory>() {{
            put(EventType.FOLLOW, new FollowFactory());
            put(EventType.UNFOLLOW, new UnfollowFactory());
            put(EventType.BROADCAST, new BroadcastFactory());
            put(EventType.PRIVATE_MSG, new PrivateMsgFactory());
            put(EventType.STATUS_UPDATE, new StatusUpdateFactory());
        }};

        final PriorityBlockingQueue<Event> readyToProcessEvents = new PriorityBlockingQueue<>();
        final PriorityBlockingQueue<Event> dispatchedEvents = new PriorityBlockingQueue<>();
        final ServerSocket userSocketServer = new ServerSocket(USER_SERVER_SOCKET_PORT);
        final ServerSocket socket = new ServerSocket(SERVER_SOCKET_PORT);

        socket.setSoTimeout(SERVER_TIMEOUT_IN_MS);
        userSocketServer.setSoTimeout(SERVER_TIMEOUT_IN_MS);

        final FollowerMazeServer eventFollowerMazeServer = new FollowerMazeServer(
                shouldKeepAlive,
                Executors.newCachedThreadPool(),
                socket,
                new IncomingEventProcessor(shouldKeepAlive, readyToProcessEvents, eventTypes));

        final FollowerMazeServer newUserFollowerMazeServer = new FollowerMazeServer(
                shouldKeepAlive,
                Executors.newCachedThreadPool(),
                userSocketServer,
                new IncomingUserClientProcessor(shouldKeepAlive, users));

        final UserResponseClientProcessor userResponseClientProcessor =
                new UserResponseClientProcessor(shouldKeepAlive, users, dispatchedEvents);

        final OutgoingEventProcessor outgoingEventProcessor =
                new OutgoingEventProcessor(shouldKeepAlive, readyToProcessEvents, dispatchedEvents);

        final List<MyAbstractServer> servers = new LinkedList<MyAbstractServer>() {{
            add(newUserFollowerMazeServer);
            add(eventFollowerMazeServer);
            add(outgoingEventProcessor);
            add(userResponseClientProcessor);
        }};


        new Application(
                shouldKeepAlive,
                Executors.newCachedThreadPool(),
                servers
        ).run();

        for (MyAbstractServer server : servers) {
            server.shutDown();
        }
    }
}
