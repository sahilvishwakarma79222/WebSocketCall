package com.alibou.websocket.service;


import org.springframework.stereotype.Service;

import com.alibou.websocket.model.Message;
import com.alibou.websocket.model.Room;
import com.alibou.websocket.model.UserSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService {
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();
    
    public Room createRoom(String roomId, String password) {
        Room room = new Room(roomId, password);
        rooms.put(roomId, room);
        System.out.println("Room created: " + roomId);
        return room;
    }
    
    public Room joinRoom(String roomId, String password, String sessionId, String username) {
        Room room = rooms.get(roomId);
        if (room == null) {
            System.out.println("Room not found: " + roomId);
            return null;
        }
        
        if (password != null && !room.getPassword().equals(password)) {
            System.out.println("Wrong password for room: " + roomId);
            return null;
        }
        
        UserSession user = new UserSession(sessionId, username, roomId);
        room.addUser(user);
        System.out.println(username + " joined room: " + roomId);
        return room;
    }
    
    public void leaveRoom(String roomId, String sessionId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            UserSession user = room.getUsers().get(sessionId);
            if (user != null) {
                System.out.println(user.getUsername() + " left room: " + roomId);
            }
            room.removeUser(sessionId);
            
            // Auto cleanup if room empty
            if (room.getUsers().isEmpty()) {
                rooms.remove(roomId);
                System.out.println("Room removed (empty): " + roomId);
            }
        }
    }
    
    public Room getRoom(String roomId) {
        return rooms.get(roomId);
    }
    
    public void addMessage(String roomId, Message message) {
        Room room = rooms.get(roomId);
        if (room != null) {
            room.addMessage(message);
        }
    }
    
    public void updatePeerId(String roomId, String sessionId, String peerId) {
        Room room = rooms.get(roomId);
        if (room != null) {
            UserSession user = room.getUsers().get(sessionId);
            if (user != null) {
                user.setPeerId(peerId);
                System.out.println("🔑 Peer ID updated: " + user.getUsername() + " -> " + peerId);
            }
        }
    }
    
    public void cleanupExpiredRooms() {
        int before = rooms.size();
        rooms.values().removeIf(Room::isExpired);
        int after = rooms.size();
        if (before != after) {
            System.out.println("Cleaned up " + (before - after) + " expired rooms");
        }
    }
}