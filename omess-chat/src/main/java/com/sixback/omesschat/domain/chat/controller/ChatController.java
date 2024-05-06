package com.sixback.omesschat.domain.chat.controller;

import com.sixback.omesschat.domain.chat.model.dto.request.api.ChatCreate;
import com.sixback.omesschat.domain.chat.model.dto.response.api.ChatDto;
import com.sixback.omesschat.domain.chat.service.ChatApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chat/{projectId}")
@RequiredArgsConstructor
public class ChatController {

    private final ChatApiService chatApiService;

    @GetMapping
    public Flux<ResponseEntity<ChatDto>> getMyChatList(
            @PathVariable Long projectId,
            @SessionAttribute Long memberId
    ) {
        return chatApiService.loadMyChatList(projectId, memberId);
    }

    @PostMapping
    public Mono<ResponseEntity<ChatDto>> createChat(
            @PathVariable Long projectId,
            @SessionAttribute Long memberId,
            @RequestBody ChatCreate create
            ) {
        return chatApiService.createChat(projectId, memberId, create);
    }

    @DeleteMapping("/{chatId}")
    public Mono<ResponseEntity<ChatDto>> leaveChat(
            @PathVariable Long projectId,
            @SessionAttribute Long memberId,
            @PathVariable String chatId
    ) {
        return chatApiService.leaveChat(projectId, memberId, chatId);
    }
}
