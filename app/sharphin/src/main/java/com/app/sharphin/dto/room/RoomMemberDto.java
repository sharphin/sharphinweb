package com.app.sharphin.dto.room;

import java.time.LocalDateTime;

public record RoomMemberDto(String room_id,
                            String room_member_id,
                            String member_auth,
                            LocalDateTime create_at) {
    
}
