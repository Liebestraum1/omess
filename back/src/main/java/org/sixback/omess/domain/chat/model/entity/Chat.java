package org.sixback.omess.domain.chat.model.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @MongoId
    private String id;

    private Long projectId;
    private List<ChatMember> members;
}
