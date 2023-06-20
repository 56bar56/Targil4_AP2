package com.example.targil4_ap2.items;

public class MessageToGet {
    private int id;
    private String created;
    private String content;
    private SenderName sender;

    public MessageToGet(int id, String created, String content, SenderName sender) {
        this.id = id;
        this.created = created;
        this.content = content;
        this.sender = sender;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(SenderName sender) {
        this.sender = sender;
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

    public SenderName getSender() {
        return sender;
    }
}
