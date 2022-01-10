package com.toby.server.controller;

import com.toby.server.pojo.Admin;
import com.toby.server.pojo.ChatMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * WebSocket
 */
@Controller
public class WsController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/ws/chat")
    public void handleMsg(Authentication authentication, ChatMsg chatMsg) {
        Admin admin = (Admin) authentication.getPrincipal();
        chatMsg.setFrom(admin.getUsername());
        chatMsg.setFromNickName(admin.getName());
        chatMsg.setDate(LocalDateTime.now());
        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(), "/queue/chat", chatMsg);
    }
}