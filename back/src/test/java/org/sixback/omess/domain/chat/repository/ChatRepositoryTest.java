package org.sixback.omess.domain.chat.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sixback.omess.domain.chat.model.entity.Chat;
import org.sixback.omess.domain.chat.model.entity.ChatMember;
import org.sixback.omess.domain.chat.model.entity.ChatRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @BeforeEach
    void setUp() {
        chatRepository.deleteAll().block();
    }

    @Test
    void shouldCreateAndFindChat() {
        // Given
        List<ChatMember> members = Arrays.asList(
                ChatMember.builder()
                        .memberId(1L)
                        .role(ChatRole.OWNER)
                        .build(),
                ChatMember.builder()
                        .memberId(2L)
                        .role(ChatRole.ADMIN)
                        .build()
        );
        Chat chat = Chat.builder()
                .projectId(1L)
                .name("Test Chat")
                .members(members)
                .build();

        // When
        Mono<Chat> savedChat = chatRepository.save(chat);

        // Then
        StepVerifier.create(savedChat)
                .assertNext(savedChatEntity -> {
                    assertThat(savedChatEntity.getProjectId()).isEqualTo(1L);
                    assertThat(savedChatEntity.getName()).isEqualTo("Test Chat");
                    assertThat(savedChatEntity.getMembers()).hasSize(2);
                })
                .verifyComplete();
    }

    @Test
    void shouldFindAllChats() {
        // Given
        Chat chat1 = Chat.builder()
                .projectId(1L)
                .name("Chat 1")
                .build();
        Chat chat2 = Chat.builder()
                .projectId(2L)
                .name("Chat 2")
                .build();

        // When
        Flux<Chat> foundChats = Flux.just(chat1, chat2)
                .flatMap(chatRepository::save)
                .thenMany(Flux.fromIterable(chatRepository.findAll().toIterable()));

        // Then
        StepVerifier.create(foundChats)
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldUpdateChat() {
        // Given
        Chat chat = Chat.builder()
                .projectId(1L)
                .name("Test Chat")
                .build();
        Mono<Chat> savedChat = chatRepository.save(chat);

        // When
        Mono<Chat> updatedChat = savedChat
                .map(c -> {
                    c.setName("Updated Chat");
                    return c;
                })
                .flatMap(chatRepository::save);

        // Then
        StepVerifier.create(updatedChat)
                .assertNext(c -> assertThat(c.getName()).isEqualTo("Updated Chat"))
                .verifyComplete();
    }

    @Test
    void shouldDeleteChat() {
        // given
        Chat chat = Chat.builder()
                .projectId(1L)
                .name("Test Chat")
                .members(List.of(ChatMember.builder().memberId(1L).role(ChatRole.OWNER).build()))
                .build();

        // when
        Mono<Chat> savedChat = chatRepository.save(chat);
        Mono<Void> deleteOperation = savedChat.flatMap(c -> chatRepository.delete(c));

        // then
        StepVerifier.create(deleteOperation)
                .expectComplete()
                .verify();
    }

    @Test
    void shouldHandleExceptions() {
        // Given
        Mono<Chat> chatMono = Mono.error(new RuntimeException("Database error"));

        // When
        Mono<Chat> exceptionHandling = chatMono
                .onErrorResume(ex -> Mono.error(new IllegalArgumentException("Handled exception")));

        // Then
        StepVerifier.create(exceptionHandling)
                .expectErrorMessage("Handled exception")
                .verify(Duration.ofSeconds(5));
    }

}