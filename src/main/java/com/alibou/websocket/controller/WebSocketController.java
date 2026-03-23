package com.alibou.websocket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.alibou.websocket.model.Message;
import com.alibou.websocket.service.RoomService;

@Controller
public class WebSocketController {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @MessageMapping("/chat")
    public void sendMessage(Message message) {
        message.setTimestamp(System.currentTimeMillis());
        roomService.addMessage(message.getRoomId(), message);
        
        // Broadcast to all users in the room
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
        System.out.println("📨 Chat message from " + message.getUsername() + ": " + message.getContent());
    }
    
    @MessageMapping("/join")
    public void joinRoom(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        headerAccessor.getSessionAttributes().put("roomId", message.getRoomId());
        headerAccessor.getSessionAttributes().put("username", message.getUsername());
        
        roomService.joinRoom(message.getRoomId(), null, sessionId, message.getUsername());
        
        // Notify others
        Message joinMessage = new Message(
            message.getRoomId(), 
            "System", 
            message.getUsername() + " joined the room", 
            "join"
        );
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), joinMessage);
        System.out.println("👤 " + message.getUsername() + " joined room " + message.getRoomId());
    }
    
    @MessageMapping("/peer-id")
    public void handlePeerId(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        roomService.updatePeerId(message.getRoomId(), sessionId, message.getPeerId());
        
        // Broadcast peer ID to ALL users in the room (including the sender)
        // This ensures everyone knows about each other
        Message peerMessage = new Message();
        peerMessage.setRoomId(message.getRoomId());
        peerMessage.setPeerId(message.getPeerId());
        peerMessage.setUsername(message.getUsername());
        peerMessage.setType("peer-id");
        
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), peerMessage);
        System.out.println("🔑 Peer ID registered: " + message.getUsername() + " -> " + message.getPeerId());
    }
    
    @MessageMapping("/leave")
    public void leaveRoom(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        roomService.leaveRoom(message.getRoomId(), sessionId);
        
        // Notify others
        Message leaveMessage = new Message(
            message.getRoomId(), 
            "System", 
            message.getUsername() + " left the room", 
            "leave"
        );
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), leaveMessage);
        System.out.println("👋 " + message.getUsername() + " left room " + message.getRoomId());
    }
    
    @MessageMapping("/signal")
    public void signal(Message message) {
        // Forward signaling data (offer, answer, ice-candidate) to the target peer
        messagingTemplate.convertAndSend("/topic/room/" + message.getRoomId(), message);
    }
}