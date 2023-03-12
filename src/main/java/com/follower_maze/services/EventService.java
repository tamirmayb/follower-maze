package com.follower_maze.services;

import com.follower_maze.entities.EventType;
import com.follower_maze.repository.Repository;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static java.util.Collections.emptySet;

public class EventService implements Runnable {

    private final static int EVENT_PORT = 9090;
    private final Repository repository;
    private static long lastSeqNo = 0L;

    public EventService(Repository repository) {
        this.repository = repository;
    }

    private void processMessage(Long seq, List<String> payload) {
        Map<Long, List<String>> seqNoToMessage = new HashMap<>(repository.getSeqNoToMessage());
        seqNoToMessage.put(seq, payload);

        while (seqNoToMessage.containsKey(lastSeqNo + 1)) {
            List<String> nextMessage = seqNoToMessage.remove(lastSeqNo + 1);
            String nextPayload = String.join("|", nextMessage);

            long seqNo = Long.parseLong(nextMessage.get(0));
            String typeStr = nextMessage.get(1);
            long fromUserId = Long.parseLong(nextMessage.get(2));
            long toUserId = Long.parseLong(nextMessage.get(3));

            Set<Long> followers = new HashSet<>();
            EventType type = EventType.of(typeStr);
            switch (type) {
                case FOLLOW:
                    followers.addAll(repository.getFollowRegistry().getOrDefault(toUserId, new HashSet<>()));
                    followers.add(fromUserId);
                    repository.getFollowRegistry().put(toUserId, followers);

                    try {
                        Socket socket = repository.getClientPool().get(toUserId);
                        checkSocketAndWrite(nextPayload, socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case UNFOLLOW:
                    followers.addAll(repository.getFollowRegistry().getOrDefault(toUserId, new HashSet<>()));
                    followers.remove(fromUserId);
                    repository.getFollowRegistry().put(toUserId, followers);
                    break;

                case BROADCAST:
                    repository.getClientPool().values().forEach(socket -> {
                        try {
                            checkSocketAndWrite(nextPayload, socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;

                case PRIVATE_MSG:
                    try {
                        Socket socket = repository.getClientPool().get(toUserId);
                        checkSocketAndWrite(nextPayload, socket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case STATUS_UPDATE:
                    followers.addAll(repository.getFollowRegistry().getOrDefault(fromUserId, emptySet()));
                    followers.forEach(follower -> {
                        try {
                            Socket socket = repository.getClientPool().get(follower);
                            checkSocketAndWrite(nextPayload, socket);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                case UNKNOWN:
                    break;
            }
            lastSeqNo = seqNo;
        }
        repository.setSeqNoToMessage(seqNoToMessage);
    }

    private static void checkSocketAndWrite(String nextPayload, Socket socket) throws IOException {
        if (socket != null) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(nextPayload + "\n");
            writer.flush();
        }
    }

    @Override
    public void run() {
        System.out.println("Listening for events on " + EVENT_PORT);
        try (Socket eventSocket = new ServerSocket(EVENT_PORT).accept()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(eventSocket.getInputStream()))) {
                reader.lines().forEach(payload -> {
                    System.out.println("Message received: " + payload);

                    List<String> payloadParts = Arrays.asList(payload.split("\\|"));
                    long seqId = Long.parseLong(payloadParts.get(0));
                    processMessage(seqId, payloadParts);
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}