package com.follower_maze.services;

import com.follower_maze.repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class UserService implements Runnable {
    private final static int CLIENT_PORT = 9099;
    private final Repository repository;

    public UserService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        System.out.println("Listening for events on " + CLIENT_PORT);
        try {
            final ServerSocket[] serverSocket = new ServerSocket[]{new ServerSocket(CLIENT_PORT)};
            final Socket[] clientSocket = {serverSocket[0].accept()};
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket[0].getInputStream()));
            reader.lines().forEach(payload -> {
                System.out.println("Message received: " + payload);

                while (repository.getClientPool() != null) {
                    process(serverSocket, clientSocket);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(ServerSocket[] serverSocket, Socket[] clientSocket) {
        try {
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(clientSocket[0].getInputStream()));
            String userId = reader.readLine();
            if (userId != null) {
                Map<Long, Socket> clientPool = repository.getClientPool();
                clientPool.put(Long.parseLong(userId), clientSocket[0]);
                System.out.println("User connected: " + userId + " (" + clientPool.size() + " total)");
            }
            clientSocket[0] = serverSocket[0].accept();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}   
