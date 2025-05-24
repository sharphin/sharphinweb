package com.app.sharphin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NotifiController {
    @GetMapping("/notif")
    public String discommu() {
        return "notifi";
    }
}
