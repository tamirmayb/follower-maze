package com.follower.maze.interfaces;

import java.io.IOException;
import java.net.Socket;

public interface ClientProcessor {
    void processEvents(Socket clientSocket) throws IOException;
}
