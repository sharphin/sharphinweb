package com.app.sharphin.controller.auth; // このファイルが属するパッケージ（フォルダ）

// 必要なクラスをインポートします
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.sharphin.dto.user.UserDto;
import com.app.sharphin.dto.user.UserSighUpDto;
import com.app.sharphin.service.UserService;

@Controller 
public class SighUpController {

    @Autowired
    private UserService userService;

    @GetMapping("/sighup") 
    public ModelAndView registerForm() {
        ModelAndView mav = new ModelAndView(); 
        mav.addObject("user",new UserSighUpDto());
        mav.setViewName("account/sighup"); 
        return mav;
    }

    @PostMapping("/sighup")
    @ResponseBody
    public int register(@ModelAttribute UserSighUpDto suser) {
        int result = userService.zeroIsAbleToRegister(suser);
        if(result != 0) return result;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDto user = new UserDto(suser.getUser_id(), 
                                suser.getUser_name(),
                                suser.getEmail(),
                                encoder.encode(suser.getPassword()),
                                null,
                                "USER",
                                false,
                                null,
                                null);
        userService.userRegister(user);
        return result;
    }
}