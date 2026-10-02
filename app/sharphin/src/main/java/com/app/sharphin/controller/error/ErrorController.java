package com.app.sharphin.controller.error;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {
    @RequestMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        // 何もしないまたは任意のレスポンスを返す
    }
    // POST で権限エラーになった場合もフォワードされるので、メソッドを限定しない
    @RequestMapping("/403")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String getMethodName() {
        return "error/error403";
    }
}
