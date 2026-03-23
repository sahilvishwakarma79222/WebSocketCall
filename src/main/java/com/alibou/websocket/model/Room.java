package com.alibou.websocket.model;
import lombok.Data;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Room {
    private String roomId;
    private String password;
    private long createdAt;
    private ConcurrentHashMap<String, UserSession> users = new ConcurrentHashMap<>();
    private CopyOnWriteArrayList<Message> messages = new CopyOnWriteArrayList<>();
    
    public Room(String roomId, String password) {
        this.roomId = roomId;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
    }
    
    public boolean isExpired() {
        // Auto cleanup after 1 hour of inactivity
        return System.currentTimeMillis() - createdAt > 3600000;
    }
    
    public void addUser(UserSession user) {
        users.put(user.getSessionId(), user);
    }
    
    public void removeUser(String sessionId) {
        users.remove(sessionId);
    }
    
    public void addMessage(Message message) {
        messages.add(message);
        // Keep only last 100 messages
        while (messages.size() > 100) {
            messages.remove(0);
        }
    }
}