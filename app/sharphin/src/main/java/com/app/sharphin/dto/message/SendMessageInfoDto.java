package com.app.sharphin.dto.message;
import java.util.List;

public record SendMessageInfoDto(SendUserDto sendUser, List<MessageDto> message) {
}
