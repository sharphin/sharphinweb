package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.room.RoomMessageDto;
import com.app.sharphin.repository.RoomMessageRepository;

@Service
public class RoomMessageService {
    @Autowired
    RoomMessageRepository repository;
    public List<RoomMessageDto> getRoomList(String user_id) {
        return repository.getMessageHistory(user_id);
    }
    public int addMessage(RoomMessageDto rooms) {
        return repository.addRoomMessage(rooms);
    }

    public int deleteRoomMessage(String room_id) {
        return repository.deleteRoomMessage(room_id);
    }
}
