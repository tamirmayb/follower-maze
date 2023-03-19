package com.follower.maze;

public class MyLogger {
    public static void log(Object loggingObjects, String message) {
        final String simpleName = loggingObjects.getClass().getSimpleName();
        System.out.println("[" + simpleName + "] " + message);
    }

}
