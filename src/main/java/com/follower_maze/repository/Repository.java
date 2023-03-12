package com.follower_maze.repository;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Repository {
    private final Map<Long, Socket> clientPool = new ConcurrentHashMap<>();
    private final Map<Long, Set<Long>> followRegistry = new HashMap<>();
    private final Map<Long, List<String>> seqNoToMessage = new HashMap<>();

    public Map<Long, Socket> getClientPool() {
        return clientPool;
    }

    public Map<Long, Set<Long>> getFollowRegistry() {
        return followRegistry;
    }

    public Map<Long, List<String>> getSeqNoToMessage() {
        return seqNoToMessage;
    }

    public void setSeqNoToMessage(Map<Long, List<String>> update) {
        seqNoToMessage.clear();
        seqNoToMessage.putAll(update);
    }

}
