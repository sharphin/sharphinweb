package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.message.SendUserDto;
import com.app.sharphin.repository.FollowUserRepository;

@Service
public class FollowUserService {
    @Autowired
    private FollowUserRepository userRepository;
    public int findFollowCount(String user_id) {
        int user = userRepository.findFollowCount(user_id);
        return user;
    }
    public int findFollowerCount(String user_id) {
        return userRepository.findFollowerCount(user_id);
    }
    public List<String> findFollowUser_idList(String user_id) {
        return userRepository.findFollowUser_idList(user_id);
    }
    public List<String> findFollowerUser_idList(String user_id) {
        return userRepository.findFollowerUser_idList(user_id);
    }
    public List<SendUserDto> messageUserlists(String from_user_id) {
        return userRepository.messageUserLists(from_user_id);
    }
}
