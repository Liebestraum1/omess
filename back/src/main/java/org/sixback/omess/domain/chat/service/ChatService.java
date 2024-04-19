package org.sixback.omess.domain.chat.service;

import org.sixback.omess.domain.chat.model.dto.response.*;
import org.sixback.omess.domain.chat.model.entity.ChatRole;
import org.sixback.omess.domain.chat.model.entity.MessageType;

import java.util.List;

public interface ChatService {

    List<ChatInfo> loadChatListByProjectIdAndMemberId(Long projectId, Long memberId);

    void createChat(Long projectId, Long memberId, List<String> emails);

    void leaveChat(Long projectId, Long roomId, Long memberId);

    List<ChatMessage> loadChatMessageList(String roomId, int offset);

    ChatMessage saveChatMessage(String roomId, Long memberId, String subject, MessageType messageType);

    ChatHeader updateHeader(String roomId, Long memberId, String subject);

    ChatNotice updateNotice(String roomId, Long memberId, String notice);

    ChatGroup loadChatMembers(String roomId);

    ChatInfo loadChatInfo(String roomId);

    RoleChange modifyRole(String roomId, String target, ChatRole role);

    ChatMessage modifyMessage(String roomId, String messageId, String subject);

    ChatMessage deleteMessage(String roomId, String messageId);
}
