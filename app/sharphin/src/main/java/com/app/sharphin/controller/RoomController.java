package com.app.sharphin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {
    @GetMapping("/myserver")
    public String requestMethodName() {
        return "myserver";
    }   
}
