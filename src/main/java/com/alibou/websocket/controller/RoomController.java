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
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @PostMapping("/create-room")
    public String createRoom(@RequestParam String password, Model model) {
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        roomService.createRoom(roomId, password);
        model.addAttribute("roomId", roomId);
        model.addAttribute("username", "Host");
        return "call";
    }
    
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
}