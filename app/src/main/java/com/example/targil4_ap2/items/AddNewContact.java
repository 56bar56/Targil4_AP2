package com.example.targil4_ap2.items;

public class AddNewContact {
    private int id;
    private UserToGet user;

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(UserToGet user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public UserToGet getUser() {
        return user;
    }

    public AddNewContact(int id, UserToGet user) {
        this.id = id;
        this.user = user;
    }

}
