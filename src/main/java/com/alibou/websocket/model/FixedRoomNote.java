package com.alibou.websocket.model;

import lombok.Data;

@Data
public class FixedRoomNote {
    private String content;
    private String lastEditedBy;
    private long lastEditedAt;
    
    public FixedRoomNote() {
        this.content = "Welcome to JyoSah! 👋\n\nShare your thoughts, links, or anything here.\n\nThis note is persistent - it stays until someone edits it!";
        this.lastEditedBy = "System";
        this.lastEditedAt = System.currentTimeMillis();
    }
    
    public FixedRoomNote(String content, String lastEditedBy) {
        this.content = content;
        this.lastEditedBy = lastEditedBy;
        this.lastEditedAt = System.currentTimeMillis();
    }
}