package com.app.sharphin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.sharphin.dto.user.UserDto;
import com.app.sharphin.dto.user.UserSighInDto;
import com.app.sharphin.dto.user.UserSighUpDto;
import com.app.sharphin.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        UserDto user = userRepository.findByUserName(user_id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with user: " + user_id);
        }
        return new UserSighInDto(user);
    }
    public int zeroIsAbleToRegister(UserSighUpDto suser) {
        if (suser.getUser_id().isEmpty()) return 1;
        if (suser.getPassword().length() < 10) return 2;
        if (suser.getUser_name().isEmpty()) return 3;
        if (suser.getEmail().isEmpty()) return 4;
        if (userRepository.findByUserName(suser.getUser_id()) != null) return -1;
        return 0;
    }

    public boolean existUser(String user_id) {
        return userRepository.findByUserName(user_id) != null;
    }
    public UserDto getUserDto(String user_id) {
        return userRepository.findByUserName(user_id);
    }
    public int userRegister(UserDto userDto) {
        return userRepository.userRegister(userDto);
    }
    public int userUpdate(UserDto userDto,String old_user_id) {
        return userRepository.userUpdate(userDto,old_user_id);
    }
    public int iconUpDate(String user_id,String icon_path) {
        return userRepository.iconUpdate(user_id, icon_path);
    }
    public List<UserDto> getUserList(String userinfo) {
        List<UserDto> users = userRepository.getUserList("user_id",userinfo);
            return users;
    }
}
