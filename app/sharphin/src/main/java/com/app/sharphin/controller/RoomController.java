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
import com.app.sharphin.service.MessageService;
import com.app.sharphin.service.RoomsService;
import com.app.sharphin.service.UserService;

@Controller
public class RoomController {
    @Autowired
    RoomsService rservice;
    @Autowired
    MessageService mservice;
    @PostMapping("/room")
    public String roomList(Model model ,@RequestParam String room_id) {
        model.addAttribute("room_list", rservice.getRoomList(room_id));
        return "room";
    }
    @PostMapping("/room/build")
    public SendMessageInfoDto roomBuild(Model model,@ModelAttribute SendUserDto sendUser) {
        return new SendMessageInfoDto(sendUser,mservice.getMessageHistory(sendUser.chatroom_id()));
    }
    @PostMapping("/room/{chatroom_id}")
    @ResponseBody
    public SendMessageInfoDto roonEnter(Model model,@ModelAttribute SendUserDto sendUser) {
        return new SendMessageInfoDto(sendUser,mservice.getMessageHistory(sendUser.chatroom_id()));
    }
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserService service;
    @MessageMapping("/room/{group_id}")
    public void sendMessage(@DestinationVariable String room_id, String message) {
        //mservice.sendMessage(room_id,message);
        if(service.existUser(room_id)) {
            simpMessagingTemplate.convertAndSend("/topic/messages/"+room_id, message);
        }
    } 
}
