package com.alibou.websocket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CleanupService {
    
    @Autowired
    private RoomService roomService;
    
    // Run cleanup every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void cleanupExpiredRooms() {
        roomService.cleanupExpiredRooms();
    }
}