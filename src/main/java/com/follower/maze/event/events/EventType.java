package com.follower.maze.event.events;

public enum EventType {
    FOLLOW ("F"),
    UNFOLLOW ("U"),
    BROADCAST ("B"),
    PRIVATE_MSG ("P"),
    STATUS_UPDATE ("S"),
    DEAD ("D");


    public final String typeCode;

    EventType(String typeCode) {
        this.typeCode = typeCode;
    }

    public static EventType valueOfTypeCode(String typeCode) {
        for (EventType e : values()) {
            if (e.typeCode.equals(typeCode)) {
                return e;
            }
        }
        return null;
    }

}
