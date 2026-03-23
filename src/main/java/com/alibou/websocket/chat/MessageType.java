// com/alibou/websocket/chat/MessageType.java
package com.alibou.websocket.chat;

public enum MessageType {
    CHAT,
    JOIN,
    LEAVE,
    IMAGE,
    USER_LIST,
    
    // 📞 Voice Call Types
    VOICE_CALL_REQUEST,
    VOICE_CALL_ACCEPT,
    VOICE_CALL_REJECT,
    VOICE_CALL_END,
    VOICE_OFFER,
    VOICE_ANSWER,
    VOICE_ICE_CANDIDATE,
    
    // 📹 Video Call Types
    VIDEO_CALL_REQUEST,
    VIDEO_CALL_ACCEPT,
    VIDEO_CALL_REJECT,
    VIDEO_CALL_END,
    VIDEO_OFFER,
    VIDEO_ANSWER,
    VIDEO_ICE_CANDIDATE
}