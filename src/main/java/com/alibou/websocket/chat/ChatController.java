package com.alibou.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private static final Set<String> activeUsers = ConcurrentHashMap.newKeySet();
    
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("📨 Received: " + chatMessage.getType() + " from: " + chatMessage.getSender() + " target: " + chatMessage.getTarget());
        
        if (chatMessage.getTarget() != null && !chatMessage.getTarget().isEmpty()) {
            // Private message - send only to target user
            String targetUser = chatMessage.getTarget().toLowerCase();
            String sender = chatMessage.getSender().toLowerCase();
            
            System.out.println("🔒 Sending private message to: " + targetUser);
            
            // Send to target user
            messagingTemplate.convertAndSendToUser(
                targetUser,
                "/queue/messages",
                chatMessage
            );
            
            // Also send back to sender (so caller knows it was sent)
            messagingTemplate.convertAndSendToUser(
                sender,
                "/queue/messages",
                chatMessage
            );
            
        } else {
            // Public message - broadcast to everyone
            System.out.println("📢 Broadcasting public message");
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
    
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String username = chatMessage.getSender().toLowerCase();
        
        activeUsers.add(username);
        headerAccessor.getSessionAttributes().put("username", username);
        
        System.out.println("✅ User joined: " + username);
        
        ChatMessage joinMessage = ChatMessage.builder()
                .type(MessageType.JOIN)
                .sender(username)
                .build();
        messagingTemplate.convertAndSend("/topic/public", joinMessage);
        
        broadcastUserList();
    }
    
    @MessageMapping("/chat.removeUser")
    public void removeUser(@Payload ChatMessage chatMessage) {
        String username = chatMessage.getSender().toLowerCase();
        activeUsers.remove(username);
        
        System.out.println("❌ User left: " + username);
        
        ChatMessage leaveMessage = ChatMessage.builder()
                .type(MessageType.LEAVE)
                .sender(username)
                .build();
        messagingTemplate.convertAndSend("/topic/public", leaveMessage);
        
        broadcastUserList();
    }
    
    private void broadcastUserList() {
        ChatMessage userListMessage = ChatMessage.builder()
                .type(MessageType.USER_LIST)
                .content(String.join(",", activeUsers))
                .sender("system")
                .build();
        messagingTemplate.convertAndSend("/topic/public", userListMessage);
    }
    
    public Set<String> getActiveUsers() {
        return activeUsers;
    }
}