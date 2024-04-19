package org.sixback.omess.domain.chat.controller;


import lombok.RequiredArgsConstructor;
import org.sixback.omess.config.websocket.StompUser;
import org.sixback.omess.domain.chat.model.dto.payload.MessagePayload;
import org.sixback.omess.domain.chat.model.dto.payload.RolePayload;
import org.sixback.omess.domain.chat.model.dto.response.*;
import org.sixback.omess.domain.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/load/{roomId}")
    @SendToUser("/sub/{roomId}")
    public List<ChatMessage> getChatHistory(@DestinationVariable String roomId, @Payload int offset) {
        return chatService.loadChatMessageList(roomId, offset);
    }

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, @Payload MessagePayload message, Principal user) {
        StompUser stompUser = (StompUser) user;
        return chatService.saveChatMessage(roomId, stompUser.getMemberId(), message.getSubject(), message.getMessageType());
    }

    @MessageMapping("/{roomId}/header")
    @SendTo("/sub/{roomId}")
    public ChatHeader changeHeader(@DestinationVariable String roomId, @Payload String subject, Principal user) {
        StompUser stompUser = (StompUser) user;
        return chatService.updateHeader(roomId, stompUser.getMemberId(), subject);
    }

    @MessageMapping("/{roomId}/notice")
    @SendTo("/sub/{roomId}")
    public ChatNotice changeNotice(@DestinationVariable String roomId, @Payload String subject, Principal user) {
        StompUser stompUser = (StompUser) user;
        return chatService.updateNotice(roomId, stompUser.getMemberId(), subject);
    }

    @MessageMapping("/{roomId}/members")
    @SendToUser("/sub/{roomId}")
    public ChatGroup getChatGroup(@DestinationVariable String roomId) {
        return chatService.loadChatMembers(roomId);
    }

    @MessageMapping("/{roomId}/info")
    @SendToUser("/sub/{roomId}")
    public ChatInfo getChatInfo(@DestinationVariable String roomId) {
        return chatService.loadChatInfo(roomId);
    }

    @MessageMapping("/{roomId}/role")
    @SendTo("/sub/{roomId}")
    public RoleChange changeRole(@DestinationVariable String roomId, @Payload RolePayload rolePayload) {
        return chatService.modifyRole(roomId, rolePayload.getTarget(), rolePayload.getTo());
    }

    @MessageMapping("/{roomId}/modify/{messageId}")
    @SendTo("/sub/{roomId}")
    public ChatMessage changeMessage(@DestinationVariable String roomId, @DestinationVariable String messageId, @Payload String subject) {
        return chatService.modifyMessage(roomId, messageId, subject);
    }

    @MessageMapping("/{roomId}/delete/{messageId}")
    @SendTo("/sub/{roomId}")
    public ChatMessage deleteMessage(@DestinationVariable String roomId, @DestinationVariable String messageId) {
        return chatService.deleteMessage(roomId, messageId);
    }
}
