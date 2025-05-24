package com.app.sharphin.dto.user;

import java.time.LocalDateTime;

public record FollowUserListDto(String follow_user_id,
                                String followed_user_id,
                                LocalDateTime create_at) {
}
