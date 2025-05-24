package com.app.sharphin.dto.user;

import java.time.LocalDateTime;

public record FollowUserDto(String follow_user_id,
                            String followed_user_id,
                            String chatroom_id,
                            boolean mutual_follow,
                            LocalDateTime create_at) {
}
