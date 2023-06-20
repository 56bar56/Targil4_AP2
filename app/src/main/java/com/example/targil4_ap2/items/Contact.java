package com.example.targil4_ap2.items;

public class Contact {
    private int id;
    private UserToGet user;
    private MessageLast lastMessage;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(UserToGet user) {
        this.user = user;
    }

    public void setLastMessage(MessageLast lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public UserToGet getUser() {
        return user;
    }

    public MessageLast getLastMessage() {
        return lastMessage;
    }

    public Contact(int id, UserToGet user, MessageLast lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }
}
