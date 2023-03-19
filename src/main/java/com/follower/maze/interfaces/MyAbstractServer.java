package com.follower.maze.interfaces;

import java.io.IOException;

public abstract class MyAbstractServer implements Runnable, ShutDown {

    public abstract void run();

    public abstract void shutDown() throws IOException;
}
