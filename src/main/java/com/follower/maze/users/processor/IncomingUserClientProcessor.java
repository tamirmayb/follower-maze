package com.follower.maze.users.processor;

import com.follower.maze.interfaces.ClientProcessor;
import com.follower.maze.MyLogger;
import com.follower.maze.users.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

public class IncomingUserClientProcessor implements ClientProcessor {
    private final Map<Integer, User> users;
    private final AtomicBoolean shouldContinueRunning;

    public IncomingUserClientProcessor(AtomicBoolean shouldContinueRunning, Map<Integer, User> users) {
        this.users = users;
        this.shouldContinueRunning = shouldContinueRunning;
    }

    public void processEvents(final Socket clientSocket) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null && shouldContinueRunning.get()) {
            try {
                final int userId = Integer.parseInt(inputLine.replace("\\r\\n", ""));
                final User noUserYet = users.get(userId);
                if (noUserYet != null) {
                    noUserYet.setWriter(writer);

                } else {
                    final User newUser = new User(userId, writer, new ConcurrentSkipListSet<User>());
                    users.put(userId, newUser);
                }
            } catch (NumberFormatException e) {
                MyLogger.log(this, "Could not read input: " + inputLine + e.getMessage());
            } catch (NullPointerException e) {
                MyLogger.log(this, "Read null here: " + inputLine);
            }
        }
    }
}
