package com.groupstudy.model;

import java.time.LocalDateTime;

public class Notification {
	
	public enum NotificationType{
		POKEMON_EVOLVED,
		TROPHY_EARNED,
		RANK_TOP,
		SESSION_END
	}
	
	private String message;
	private NotificationType type;
	private LocalDateTime timestamp;
	
	public Notification(String message, NotificationType type) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }
        
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
	
	public String getMessage() {
        return message;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s", type, message);
    }
    
	
}
