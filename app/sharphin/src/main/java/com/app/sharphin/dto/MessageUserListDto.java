package com.app.sharphin.dto;

import java.time.LocalDateTime;

public record MessageUserListDto(String user_id1,
                                 String user_id2,
                                 LocalDateTime create_at) {
    
}
