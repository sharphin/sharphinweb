package com.app.sharphin.controller;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.sharphin.dto.message.SendMessageInfoDto;
import com.app.sharphin.dto.message.SendUserDto;
import com.app.sharphin.dto.user.UserSighInDto;
import com.app.sharphin.service.FollowUserService;
import com.app.sharphin.service.MessageService;

@Controller
public class DirectMessageController {
    @Autowired
    FollowUserService fservice;
    @Autowired
    MessageService mservice;
    @PostMapping("/message")
    public String message(Model model, @AuthenticationPrincipal UserSighInDto loginUser) {
        model.addAttribute("user_list", fservice.messageUserlists(loginUser.getUser_id()));
        return "message";
    }
    @PostMapping("/message/d/{chatroom_id}")
    @ResponseBody
    public SendMessageInfoDto messages(@PathVariable String chatroom_id, @AuthenticationPrincipal UserSighInDto loginUser) {
        SendUserDto partner = fservice.findChatPartner(loginUser.getUser_id(), chatroom_id);
        if (partner == null) throw new AccessDeniedException("not a member of chatroom: " + chatroom_id);
        return new SendMessageInfoDto(partner,mservice.getMessageHistory(chatroom_id));
    }
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // 宛先はクライアント指定 ({to}) ではなく、送信者が参加している chatroom の相手から決める
    @MessageMapping("/chat/{chatroom_id}/{to}")
    public void sendMessage(@DestinationVariable String chatroom_id, String message, Principal principal) {
        SendUserDto partner = fservice.findChatPartner(principal.getName(), chatroom_id);
        if (partner == null) return;
        mservice.sendMessage(chatroom_id,partner.user_id(),message);
        simpMessagingTemplate.convertAndSendToUser(partner.user_id(), "/queue/messages", message);
    }
}
