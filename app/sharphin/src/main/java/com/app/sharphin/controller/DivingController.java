package com.app.sharphin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DivingController {
    
    @GetMapping("/diving")
    public String diving() {
        return "diving";
    }
}
