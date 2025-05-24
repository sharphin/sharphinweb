package com.app.sharphin.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SighInController {

    @GetMapping("/sighin")
    public String login() {
        return "account/sighin";
    }
}
