package org.sixback.omess.domain.chat.model.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Chat {

    @Id
    private String chatId;
    private Long projectId;
    private String name;
    private Content header;
    private Content notice;
    private List<ChatMember> members;
    private List<Content> messages;

    public Chat() {
    }

    @Builder
    public Chat(Long projectId, String name, Content header, Content notice, List<ChatMember> members,
                List<Content> messages) {
        this.projectId = projectId;
        this.name = name;
        this.header = header;
        this.notice = notice;
        this.members = members;
        this.messages = messages;
    }
}
