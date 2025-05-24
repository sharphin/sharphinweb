package com.app.sharphin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DisCommuController {
    
    @GetMapping("/discom")
    public String discommu() {
        return "discomm";
    }
}