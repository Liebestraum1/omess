package com.sixback.omesschat.domain.chat.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @MongoId
    private String id;
    private Content header;
    private Long projectId;
    private List<ChatMember> members;

    public Chat(Long projectId, List<ChatMember> members) {
        this.projectId = projectId;
        this.members = members;
    }

    public Chat update(Content header) {
        this.header = header;
        return this;
    }
}
