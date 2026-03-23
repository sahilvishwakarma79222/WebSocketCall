package com.alibou.websocket.chat;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String target;     // 🔥 ADD THIS FIELD
    private String timestamp;   // 🔥 ADD THIS FIELD (optional but useful)
}