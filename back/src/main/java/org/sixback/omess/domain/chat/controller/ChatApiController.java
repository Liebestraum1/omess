package org.sixback.omess.domain.chat.controller;


import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.chat.model.dto.request.ChatCreateRequest;
import org.sixback.omess.domain.chat.model.dto.response.ChatInfo;
import org.sixback.omess.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/{projectId}")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatInfo>> getMyChatList(@PathVariable Long projectId, @SessionAttribute Long memberId) {
        return ResponseEntity.ok(chatService.loadChatListByProjectIdAndMemberId(projectId, memberId));
    }

    @PostMapping
    public ResponseEntity<Void> createChat(
            @PathVariable Long projectId,
            @SessionAttribute Long memberId,
            @RequestBody ChatCreateRequest request
    ) {
        chatService.createChat(projectId, memberId, request.getEmails());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> leaveChat(
            @PathVariable Long projectId,
            @PathVariable Long roomId,
            @SessionAttribute Long memberId
    ) {
        chatService.leaveChat(projectId, roomId, memberId);
        return ResponseEntity.ok().build();
    }
}
