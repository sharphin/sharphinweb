package com.app.sharphin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.message.MessageDto;
import com.app.sharphin.repository.MessageRepository;
@Service
public class MessageService {
    @Autowired
    MessageRepository mrepository;
    public List<MessageDto> getMessageHistory(String chatroom_id) {
        return mrepository.getMessageHistory(chatroom_id);
    }
    public int sendMessage(String chatroom_id,String recieve_user_id,String message) {
        return mrepository.addMessage(new MessageDto(chatroom_id,recieve_user_id,message,NOW_TIME()));
    }
    private LocalDateTime NOW_TIME() {
        return LocalDateTime.now();
    }
}
