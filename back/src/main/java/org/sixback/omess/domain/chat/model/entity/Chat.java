package org.sixback.omess.domain.chat.model.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter
public class Chat {

    @MongoId
    private String chatId;
    private Long projectId;
    @Setter
    private String name;
    @Setter
    private Content header;
    @Setter
    private Content notice;
    private List<ChatMember> members;
    private List<Content> messages = new ArrayList<>();

    protected Chat() {
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
