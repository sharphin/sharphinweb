package com.app.sharphin.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.sharphin.dto.message.SendMessageInfoDto;
import com.app.sharphin.dto.message.SendUserDto;
import com.app.sharphin.service.FollowUserService;
import com.app.sharphin.service.MessageService;
import com.app.sharphin.service.UserService;

@Controller
public class DirectMessageController {
    @Autowired
    FollowUserService fservice;
    @Autowired
    MessageService mservice;
    @PostMapping("/message")
    public String message(Model model ,@RequestParam String from_user_id) {
        model.addAttribute("user_list", fservice.messageUserlists(from_user_id));
        return "message";
    }
    @PostMapping("/message/d/{chatroom_id}")
    @ResponseBody
    public SendMessageInfoDto messages(Model model,@ModelAttribute SendUserDto sendUser) {
        return new SendMessageInfoDto(sendUser,mservice.getMessageHistory(sendUser.chatroom_id()));
    }
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserService service;
    @MessageMapping("/chat/{chatroom_id}/{to}")
    public void sendMessage(@DestinationVariable String chatroom_id,@DestinationVariable String to, String message) {
        mservice.sendMessage(chatroom_id,to,message);
        if(service.existUser(to)) {
            simpMessagingTemplate.convertAndSend("/topic/messages/"+to, message);
        }
    }
}