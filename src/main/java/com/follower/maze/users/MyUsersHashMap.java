package com.follower.maze.users;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class MyUsersHashMap implements Map<Integer, User> {
    private final Map<Integer, User> users;

    public MyUsersHashMap(Map<Integer, User> users) {
        this.users = users;
    }

    @Override
    public int size() {
        return users.size();
    }

    @Override
    public boolean isEmpty() {
        return users.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return users.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return users.containsValue(value);
    }

    @Override
    public User get(Object key) {
        User newUser = users.get(key);
        final int userId = (int) key;
        if (newUser == null) {
            final User userToBeCreated = new User(userId, new LinkedList<String>(), new ConcurrentSkipListSet<User>());
            users.put(userId, userToBeCreated);
            newUser = userToBeCreated;
        }
        return newUser;
    }

    @Override
    public User put(Integer key, User value) {
        return users.put(key, value);
    }

    @Override
    public User remove(Object key) {
        return users.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends User> m) {
        users.putAll(m);
    }

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return users.keySet();
    }

    @Override
    public Collection<User> values() {
        return users.values();
    }

    @Override
    public Set<Entry<Integer, User>> entrySet() {
        return users.entrySet();
    }
}
