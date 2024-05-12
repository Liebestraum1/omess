package com.sixback.omesschat.domain.chat.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @MongoId
    private String id;
    private Content header = Content.empty();
    private Long projectId;
    private List<ChatMember> members;
    private Long pinCount = 0L;

    @Setter
    private String name;

    public Chat(Long projectId, String name, List<ChatMember> members) {
        this.projectId = projectId;
        this.name = name;
        this.members = members;
    }

    public Chat update(Content header) {
        this.header = header;
        return this;
    }

    public Chat update(String name) {
        this.name = name;
        return this;
    }

    public Chat pinned() {
        this.pinCount++;
        return this;
    }

    public Chat unpinned() {
        this.pinCount--;
        return this;
    }
}
