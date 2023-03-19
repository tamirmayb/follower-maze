package com.follower.maze.event.events;

import com.follower.maze.users.MyUsersHashMap;
import com.follower.maze.users.User;
import org.junit.Test;
import org.mockito.internal.verification.Times;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UnfollowTest {

    private final User newUser3 = mock(User.class);
    private final User newUser2 = mock(User.class);
    private final Map<Integer, User> users = new MyUsersHashMap(new HashMap<Integer, User>() {{
        put(2, newUser2);
        put(3, newUser3);
    }});
    private final Unfollow unfollow = new Unfollow(1, "1|F|2|3", 2, 3);
    private final HashMap<Integer, User> noUsers = new HashMap<>();

    @Test
    public void testNotifyUsers() throws Exception {

        unfollow.sendEventToUsers(users);

        verify(newUser3, new Times(0)).notifyFollowers(anyString());
    }

    @Test
    public void testFollowUserCorrectly() throws Exception {

        unfollow.sendEventToUsers(users);

        verify(newUser3).removeFollower(newUser2);
    }

}