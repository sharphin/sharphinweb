package com.app.sharphin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.sharphin.dto.user.UserSighInDto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserSighInDto userDetails) {
        return "home";
    }
}
