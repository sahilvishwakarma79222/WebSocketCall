package com.alibou.websocket.model;

import lombok.Data;

@Data
public class UserSession {
    private String sessionId;
    private String username;
    private String roomId;
    private long joinedAt;
    private String peerId;
    
    public UserSession(String sessionId, String username, String roomId) {
        this.sessionId = sessionId;
        this.username = username;
        this.roomId = roomId;
        this.joinedAt = System.currentTimeMillis();
    }
}