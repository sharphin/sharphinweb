package com.app.sharphin.controller.error;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ErrorController {
    @RequestMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // 何もしないまたは任意のレスポンスを返す
    }
    @GetMapping("/403")
    public String getMethodName() {
        return "error/error403";
    }
}
