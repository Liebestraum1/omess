package org.sixback.omess.domain.chat.controller;


import lombok.RequiredArgsConstructor;
import org.sixback.omess.domain.chat.model.dto.request.ChatCreateRequest;
import org.sixback.omess.domain.chat.model.dto.response.ChatInfo;
import org.sixback.omess.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chat/{projectId}")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatApiController {

    private final ChatService chatService;

    @GetMapping
    public Flux<ResponseEntity<ChatInfo>> getMyChatList(@PathVariable Long projectId, @SessionAttribute(required = false) Long memberId) {
        if (memberId == null) memberId = 1L;
        return chatService.loadChatListByProjectIdAndMemberId(projectId, memberId).map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<ChatInfo>> createChat(
            @PathVariable Long projectId,
            @SessionAttribute Long memberId,
            @RequestBody ChatCreateRequest request
    ) {
        return chatService.createChat(projectId, memberId, request.getName(), request.getEmails())
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{roomId}")
    public Mono<ResponseEntity<Void>> leaveChat(
            @PathVariable String roomId,
            @SessionAttribute Long memberId
    ) {
        return chatService.leaveChat(roomId, memberId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
