package com.alibou.websocket.controller;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alibou.websocket.model.FixedRoomNote;
import com.alibou.websocket.service.FixedRoomNoteService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/secret/note")
public class FixedRoomNoteController {
    
    @Autowired
    private FixedRoomNoteService noteService;
    
    // Get note as JSON (for frontend)
    @GetMapping("/get")
    @ResponseBody
    public Map<String, Object> getNote() {
        FixedRoomNote note = noteService.getNote();
        Map<String, Object> response = new HashMap<>();
        response.put("content", note.getContent());
        response.put("lastEditedBy", note.getLastEditedBy());
        response.put("lastEditedAt", note.getLastEditedAt());
        return response;
    }
    
    // Update note
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateNote(@RequestParam String content, 
                                          @RequestParam String editedBy) {
        FixedRoomNote note = noteService.updateNote(content, editedBy);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("content", note.getContent());
        response.put("lastEditedBy", note.getLastEditedBy());
        response.put("lastEditedAt", note.getLastEditedAt());
        return response;
    }
}