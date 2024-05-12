package com.sixback.omesschat.domain.chat.controller;

import com.sixback.omesschat.domain.chat.model.dto.request.api.ChatCreate;
import com.sixback.omesschat.domain.chat.model.dto.response.api.ChatDto;
import com.sixback.omesschat.domain.chat.service.ChatApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/chat/{projectId}")
@RequiredArgsConstructor
public class ChatController {

    private final ChatApiService chatApiService;

    @GetMapping
    public Mono<ResponseEntity<Flux<ChatDto>>> getMyChatList(
            @PathVariable Long projectId,
            @RequestAttribute(name = "memberId") Long memberId
    ) {
        Flux<ChatDto> chatDtoFlux = chatApiService.loadMyChatList(projectId, memberId);
        return Mono.just(ResponseEntity.ok(chatDtoFlux));
    }

    @PostMapping
    public Mono<ResponseEntity<ChatDto>> createChat(
            @RequestAttribute(name = "memberId") Long memberId,
            @PathVariable Long projectId,
            @RequestBody @Valid ChatCreate create
            ) {
        return chatApiService.createChat(projectId, memberId, create);
    }

    @DeleteMapping("/{chatId}")
    public Mono<ResponseEntity<ChatDto>> leaveChat(
            @PathVariable Long projectId,
            @RequestAttribute(name = "memberId") Long memberId,
            @PathVariable String chatId
    ) {
        return chatApiService.leaveChat(projectId, memberId, chatId)
                .doOnSuccess(chatDtoResponseEntity -> log.info(chatDtoResponseEntity.toString()));
    }
}
