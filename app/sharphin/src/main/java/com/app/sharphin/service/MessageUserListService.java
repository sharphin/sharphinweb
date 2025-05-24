package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.ViewMessageUserListDto;
import com.app.sharphin.repository.MessageUserListRepository;

@Service
public class MessageUserListService {
    @Autowired
    private MessageUserListRepository userList;
    public List<ViewMessageUserListDto> messageUserIcons(String from_user_id) {
        return userList.messageUserIcons(from_user_id);
    }
}
