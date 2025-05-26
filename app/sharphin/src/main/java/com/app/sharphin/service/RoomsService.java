package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.room.RoomsDto;
import com.app.sharphin.repository.RoomsRepository;

@Service
public class RoomsService {
    @Autowired
    RoomsRepository repository;
    public List<RoomsDto> getRoomList(String user_id) {
        return repository.getRoomList(user_id);
    }
    public int buildRoom(RoomsDto rooms) {
        return repository.buildRoom(rooms);
    }
    public int updateRoom(RoomsDto rooms) {
       return repository.updateRoom(rooms); 
    }
    public int deleteRoom(String room_id) {
        return repository.deleteRoom(room_id);
    }
}
