package com.app.sharphin.dto.message;

public record SendUserDto(String user_id,
                          String user_name,
                          String icon_path,
                          String chatroom_id,
                          String latest_message) {
    
}
