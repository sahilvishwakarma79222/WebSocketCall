package com.alibou.websocket.service;

 import com.alibou.websocket.model.FixedRoomNote;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
 import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FixedRoomNoteService {
    
    private FixedRoomNote currentNote;
    private static final String NOTE_FILE = "fixed_room_note.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostConstruct
    public void init() {
        loadNoteFromFile();
    }
    
    private void loadNoteFromFile() {
        try {
            File file = new File(NOTE_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(NOTE_FILE)));
                currentNote = objectMapper.readValue(content, FixedRoomNote.class);
                System.out.println("✅ Note loaded from file");
            } else {
                currentNote = new FixedRoomNote();
                saveNoteToFile();
                System.out.println("✅ New note created");
            }
        } catch (IOException e) {
            System.err.println("❌ Error loading note: " + e.getMessage());
            currentNote = new FixedRoomNote();
        }
    }
    
    private void saveNoteToFile() {
        try {
            objectMapper.writeValue(new File(NOTE_FILE), currentNote);
            System.out.println("✅ Note saved to file");
        } catch (IOException e) {
            System.err.println("❌ Error saving note: " + e.getMessage());
        }
    }
    
    public FixedRoomNote getNote() {
        return currentNote;
    }
    
    public FixedRoomNote updateNote(String newContent, String updatedBy) {
        currentNote.setContent(newContent);
        currentNote.setLastEditedBy(updatedBy);
        currentNote.setLastEditedAt(System.currentTimeMillis());
        saveNoteToFile();
        return currentNote;
    }
}