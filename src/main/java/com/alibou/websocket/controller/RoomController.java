package com.alibou.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibou.websocket.model.Room;
import com.alibou.websocket.service.RoomService;

import java.util.UUID;

@Controller
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    // ========== FIXED ROOM CONFIGURATION (Sirf Aapko Pata) ==========
    private static final String FIXED_ROOM_ID = "jyoti";
    private static final String FIXED_ROOM_PASSWORD = "pagli";
    // =================================================================
    
    @GetMapping("/")
    public String index() {
        // Auto-create fixed room on startup (hidden from users)
        if (roomService.getRoom(FIXED_ROOM_ID) == null) {
            roomService.createRoom(FIXED_ROOM_ID, FIXED_ROOM_PASSWORD);
            System.out.println("✅ Fixed room created: " + FIXED_ROOM_ID);
        }
        return "index";
    }
    
    // Normal create room - Users ko option milega
    @PostMapping("/create-room")
    public String createRoom(@RequestParam String password, Model model) {
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        roomService.createRoom(roomId, password);
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", "Host");
        return "call";
    }
    
    // Normal join room - Users ko join karne do
    @PostMapping("/join-room")
    public String joinRoom(@RequestParam String roomId, 
                          @RequestParam String password,
                          @RequestParam String username,
                          Model model) {
        
        Room room = roomService.getRoom(roomId);
        
        if (room == null) {
            model.addAttribute("error", "Room not found!");
            return "index";
        }
        
        if (!room.getPassword().equals(password)) {
            model.addAttribute("error", "Wrong password!");
            return "index";
        }
        
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", username);
        return "call";
    }
    
    // ========== SECRET ENDPOINT FOR FIXED ROOM (Sirf Aap Use Karo) ==========
    // Ye endpoint kisi ko dikhega nahi. Aap ise use karo fixed room join karne ke liye.
    @GetMapping("/secret/join-fixed")
    public String joinFixedRoom(@RequestParam String username, Model model) {
        // Ensure fixed room exists
        if (roomService.getRoom(FIXED_ROOM_ID) == null) {
            roomService.createRoom(FIXED_ROOM_ID, FIXED_ROOM_PASSWORD);
        }
        model.addAttribute("roomId", FIXED_ROOM_ID);
        model.addAttribute("username", username);
        return "call";
    }
    
    // Secret endpoint to get fixed room details (for your reference)
    @GetMapping("/secret/fixed-room-info")
    @ResponseBody
    public String getFixedRoomInfo() {
        return "Fixed Room ID: " + FIXED_ROOM_ID + "\nPassword: " + FIXED_ROOM_PASSWORD;
    }
}