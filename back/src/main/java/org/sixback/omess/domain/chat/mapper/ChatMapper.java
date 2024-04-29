package org.sixback.omess.domain.chat.mapper;

import org.sixback.omess.domain.chat.model.dto.response.*;
import org.sixback.omess.domain.chat.model.entity.*;
import org.sixback.omess.domain.member.model.dto.response.GetMemberResponse;

import java.util.List;

public class ChatMapper {

    public static Chat toChat(Long projectId, String name, List<ChatMember> members) {
        return Chat.builder()
                .projectId(projectId)
                .name(name)
                .members(members)
                .build();
    }

    public static ChatInfo chatToChatInfo(Chat chat) {
        return ChatInfo.builder()
                .id(chat.getChatId())
                .name(chat.getName())
                .memberCount(chat.getMembers().size())
                .build();
    }

    public static Content toContent(Long memberId, String content) {
        return Content.builder()
                .memberId(memberId)
                .content(content)
                .build();
    }

    public static RoleChange toRoleChange(ChatSubscriber subscriber, ChatRole role) {
        return RoleChange.builder()
                .target(subscriber)
                .to(role)
                .build();
    }

    public static DeletedMessage contentToDeletedMessage(String contentId) {
        return DeletedMessage.builder()
                .messageId(contentId)
                .build();
    }
}
