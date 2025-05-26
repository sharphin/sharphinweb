package com.app.sharphin.dto.room;

import java.time.LocalDateTime;

public record RoomsDto(String room_id,
                       String room_owner,
                       String room_name,
                       String icon_path,
                       LocalDateTime create_at) {
    
}
