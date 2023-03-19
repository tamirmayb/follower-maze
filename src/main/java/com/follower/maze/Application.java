package com.follower.maze;

import com.follower.maze.interfaces.MyAbstractServer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class Application {


    private final ExecutorService serverThreadPool;
    private final List<MyAbstractServer> servers;
    private final AtomicBoolean shouldContinueRunning;

    public Application(AtomicBoolean shouldContinueRunning, ExecutorService applicationThreads, List<MyAbstractServer> servers) {
        this.servers = servers;
        this.serverThreadPool = applicationThreads;
        this.shouldContinueRunning = shouldContinueRunning;
    }

    public void run() {
        for (Runnable runnable : servers) {
            serverThreadPool.submit(runnable);
        }
        while (shouldContinueRunning.get()) {
            serverThreadPool.shutdownNow();
        }
    }
}
