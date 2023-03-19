package com.follower.maze.event.events;

import com.follower.maze.users.MyUsersHashMap;
import com.follower.maze.users.User;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PrivateMsgTest {

    private final User newUserThree = mock(User.class);
    private final User newUserTwo = mock(User.class);
    private final Map<Integer, User> twoUsers = new MyUsersHashMap(new HashMap<Integer, User>() {{
        put(2, newUserTwo);
        put(3, newUserThree);
    }});
    private final PrivateMsg privateMsg = new PrivateMsg(1, "1|F|2|3", 2, 3);

    @Test
    public void testNotifyUsers() throws Exception {

        privateMsg.sendEventToUsers(twoUsers);

        verify(newUserThree).receiveEvent("1|F|2|3");
    }

}