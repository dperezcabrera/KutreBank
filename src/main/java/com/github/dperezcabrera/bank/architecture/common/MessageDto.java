package com.github.dperezcabrera.bank.architecture.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class MessageDto {
    private MessageType type;
    private String description;

    private MessageDto(MessageType type, String description) {
        this.type = type;
        this.description = description;
    }

    public static MessageDto info(String description) {
        return new MessageDto(MessageType.INFO, description);
    }

    public static MessageDto error(String description) {
        return new MessageDto(MessageType.ERROR, description);
    }

    public static MessageDto forbidden(String description) {
        return new MessageDto(MessageType.FORBIDDEN, description);
    }

    public ResponseEntity<MessageDto> toResponse() {
        switch (type) {
            case INFO:
                return ResponseEntity.ok(this);
            case FORBIDDEN:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(this);
        }
    }
}
