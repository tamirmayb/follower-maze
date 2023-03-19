package com.follower.maze;

import com.follower.maze.interfaces.ClientProcessor;
import com.follower.maze.interfaces.MyAbstractServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class FollowerMazeServer extends MyAbstractServer {
    private final ExecutorService userThreadPool;
    private final ServerSocket socket;
    private final AtomicBoolean shouldContinueRunning;
    private final ClientProcessor clientProcessor;

    public FollowerMazeServer(AtomicBoolean shouldContinueRunning, ExecutorService userThreadPool,
                              ServerSocket socket,
                              ClientProcessor clientProcessor) {
        this.socket = socket;
        this.userThreadPool = userThreadPool;
        this.clientProcessor = clientProcessor;
        this.shouldContinueRunning = shouldContinueRunning;
    }

    @Override
    public void run() {
        while (shouldContinueRunning.get()) {
            final Socket userSocket;
            try {
                userSocket = socket.accept();
                MyLogger.log(this, "listening for " + clientProcessor);
                userThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            clientProcessor.processEvents(userSocket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                });
            } catch (IOException e) {
                MyLogger.log(this, "Server timed out " + clientProcessor);
            }
        }
        MyLogger.log(this, "Exiting from " + clientProcessor);
    }

    public void shutDown() throws IOException {
        userThreadPool.shutdownNow();
        socket.close();
    }
}
