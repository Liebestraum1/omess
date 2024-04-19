package org.sixback.omess.config.websocket;

import lombok.Getter;

import java.security.Principal;
import java.util.UUID;

@Getter
public class StompUser implements Principal {

    private final String name = UUID.randomUUID().toString();
    private final Long memberId;

    public StompUser(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String getName() {
        return name;
    }
}
