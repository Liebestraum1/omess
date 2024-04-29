package org.sixback.omess.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sixback.omess.domain.chat.mapper.ChatMapper;
import org.sixback.omess.domain.chat.mapper.ChatMemberMapper;
import org.sixback.omess.domain.chat.mapper.ChatMessageMapper;
import org.sixback.omess.domain.chat.model.dto.payload.MessagePayload;
import org.sixback.omess.domain.chat.model.dto.response.*;
import org.sixback.omess.domain.chat.model.entity.*;
import org.sixback.omess.domain.chat.repository.ChatMessageRepository;
import org.sixback.omess.domain.chat.repository.ChatRepository;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatMemberService chatMemberService;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public Flux<ChatInfo> loadChatListByProjectIdAndMemberId(Long projectId, Long memberId) {
        return chatRepository.findAllByProjectIdAndMemberId(projectId, memberId)
                .map(ChatMapper::chatToChatInfo);
    }

    public Mono<ChatInfo> createChat(Long projectId, Long memberId, String name, List<String> emails) {
        List<ChatMember> members = chatMemberService.createChatMember(memberId, emails);
        Chat chat = ChatMapper.toChat(projectId, name, members);
        return chatRepository.save(chat).map(createdChat -> ChatMapper.chatToChatInfo(chat));
    }

    public Mono<Void> leaveChat(String chatId, Long memberId) {
        return chatRepository.findById(chatId)
                .flatMap(chat -> {
                    ChatMember chatMember = chat.getMembers().stream()
                            .filter(m -> m.getMemberId().equals(memberId))
                            .findAny()
                            .orElseThrow(IllegalArgumentException::new);
                    chatMember.setIsAlive(false);
                    return chatRepository.save(chat); // 변경된 chat 객체를 저장
                }).then();
    }

    @Transactional(readOnly = true)
    public Flux<ChatMessageDto> loadChatMessageList(String chatId, int offset) {
        Pageable of = PageRequest.of(offset, 10, Sort.by(Sort.Direction.DESC, "createAt"));
//        return chatMessageRepository.findChatMessagesByChatId(chatId)
//                .map(chat -> {
//                    Long memberId = chat.getMemberId();
//                    GetMemberResponse member = chatMemberService.findMemberById(memberId);
//                    return ChatMessageMapper.toResponse(chat, member, ResponseType.MESSAGES);
//                });
        chatMessageRepository.findChatMessagesByChatId(chatId).subscribe(chatMessage -> {
            System.out.println(chatMessage.getMessage());
        });
        return Flux.just();
    }

    public Mono<ChatMessageDto> saveChatMessage(String roomId, Long memberId, String message, MessageType messageType) {
        return chatMessageRepository.save(ChatMessageMapper.entityFrom(roomId, memberId, message, messageType))
                .map(chat -> {
                    GetMemberResponse member = chatMemberService.findMemberById(memberId);
                    return ChatMessageMapper.toResponse(chat, member, ResponseType.MESSAGE);
                });
    }

//    public Mono<ContentDto> update(String roomId, Long memberId, String subject, MessageType type) {
//        return chatRepository.findById(roomId).handle((chat, sink) -> {
//            Content content = ChatMapper.toContent(memberId, subject, type);
//            ResponseType responseType = switch (type) {
//                case MessageType.HEADER -> {
//                    chat.setHeader(content);
//                    yield ResponseType.UPDATE_HEADER;
//                }
//                case MessageType.NOTICE -> {
//                    chat.setNotice(content);
//                    yield ResponseType.UPDATE_NOTICE;
//                }
//                default -> {
//                    {
//                        sink.error(new IllegalArgumentException());
//                        yield null;
//                    }
//                }
//            };
//            sink.next(ChatMapper.contentToChatMessage(content, chatMemberService.findMemberById(memberId), responseType));
//        });
//    }

    @Transactional(readOnly = true)
    public Flux<ChatSubscriber> loadChatMembers(String chatId) {
        return chatRepository.findMembersByChatId(chatId)
                .map(chatMember -> {
                    Long memberId = chatMember.getMemberId();
                    GetMemberResponse member = chatMemberService.findMemberById(memberId);
                    return ChatMemberMapper.memberToChatSubscriber(member);
                });
    }

    @Transactional(readOnly = true)
    public Mono<ChatInfo> loadChatInfo(String chatId) {
        return chatRepository.findById(chatId).map(ChatMapper::chatToChatInfo);
    }

    public Mono<RoleChange> modifyRole(String chatId, Long target, ChatRole role, Long memberId) {
        return chatRepository.findById(chatId).handle((chat, sink) -> {
            ChatMember src = chat.getMembers()
                    .stream()
                    .filter(m -> m.getMemberId().equals(memberId)).findAny()
                    .orElseThrow();
            if (!(src.getRole() == ChatRole.OWNER || src.getRole() == ChatRole.ADMIN)) {
                sink.error(new IllegalArgumentException());
                return;
            }

            GetMemberResponse member = chatMemberService.findMemberById(target);
            ChatMember chatMember = chat.getMembers()
                    .stream()
                    .filter(m -> m.getMemberId().equals(target)).findAny()
                    .orElseThrow();
            chatMember.setRole(role);
            ChatSubscriber chatSubscriber = ChatMemberMapper.memberToChatSubscriber(member);
            sink.next(ChatMapper.toRoleChange(chatSubscriber, role));
        });
    }

    public Mono<ChatMessageDto> modifyMessage(String messageId, Long memberId, MessagePayload message) {
        return chatMessageRepository.findById(messageId).handle((chat, sink) -> {
            if (!chat.getMemberId().equals(memberId)) {
                sink.error(new IllegalArgumentException());
                return;
            }
            chat.setIsModified(true);
            chat.setMessage(message.getSubject());
            GetMemberResponse member = chatMemberService.findMemberById(memberId);
            sink.next(ChatMessageMapper.toResponse(chat, member, ResponseType.MESSAGE));
        });
    }

    public Mono<DeletedMessage> deleteMessage(String messageId, Long memberId) {
        return chatMessageRepository.findById(messageId).handle((chat, sink) -> {
            if (!chat.getMemberId().equals(memberId)) {
                sink.error(new IllegalArgumentException());
                return;
            }
            chat.setIsDeleted(true);
            sink.next(ChatMapper.contentToDeletedMessage(messageId));
        });
    }
}
