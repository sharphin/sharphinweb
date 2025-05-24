package com.app.sharphin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin/admin";
    }
    @GetMapping("/admin/user_search")
    public String getUserSearch(Model model) {
        return "admin/user_search";
    }
    @GetMapping("/admin/post_search")
    public String getPostSearch(Model model) {
        return "admin/post_search";
    }
    @GetMapping("/admin/room_search")
    public String getRoomSearch(Model model) {
        return "admin/room_search";
    }
}