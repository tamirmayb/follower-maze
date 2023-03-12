package com.follower_maze;

import com.follower_maze.repository.Repository;
import com.follower_maze.services.EventService;
import com.follower_maze.services.UserService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class application {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new UserService(new Repository()));
        executor.submit(new EventService(new Repository()));
    }
}
