package com.app.sharphin.dto.message;

import java.time.LocalDateTime;

public record MessageDto(String chatroom_id,
                         String recieve_user_id,
                         String main_message,
                         LocalDateTime create_at) {
}
