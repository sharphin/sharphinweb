package com.app.sharphin.dto.room;

import java.time.LocalDateTime;

public record RoomMessageDto(String room_id,
                             String send_user_id,
                             String main_message,
                             LocalDateTime create_at) {
    
}
