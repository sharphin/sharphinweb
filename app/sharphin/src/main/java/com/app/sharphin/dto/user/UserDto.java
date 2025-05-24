package com.app.sharphin.dto.user;

import java.time.LocalDateTime;

public record UserDto(String user_id,
                      String user_name,
                      String email,
                      String password,
                      String icon_path,
                      String authority,
                      Boolean disable,
                      LocalDateTime create_at,
                      LocalDateTime update_at) {
}
