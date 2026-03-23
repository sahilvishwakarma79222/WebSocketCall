package com.alibou.websocket.model;

import lombok.Data;

@Data
public class Message {
    private String roomId;
    private String username;
    private String content;
    private String type;
    private long timestamp;
    private String peerId;
    private Object data;
    
    public Message() {}
    
    public Message(String roomId, String username, String content, String type) {
        this.roomId = roomId;
        this.username = username;
        this.content = content;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
}