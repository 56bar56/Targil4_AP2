package com.example.targil4_ap2.items;

public class MessageLast {
    private int id;
    private String created;
    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getContent() {
        return content;
    }

    public MessageLast(int id, String created, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
    }
}
