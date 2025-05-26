package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.room.RoomMemberDto;
import com.app.sharphin.repository.RoomMemberRepository;

@Service
public class RoomMemberService {
    @Autowired
    RoomMemberRepository repository;
    public List<RoomMemberDto> getRoomList(String user_id) {
        return repository.getRoomMember(user_id);
    }
    public int addMessage(RoomMemberDto rooms) {
        return repository.addRoomMember(rooms);
    }
    public int deleteRoomMember(String room_id) {
        return repository.deleteRoomMember(room_id);
    }
    public int deleteRoomMember(String room_id,String member_id) {
        return repository.deleteRoomMember(room_id,member_id);
    }
}
