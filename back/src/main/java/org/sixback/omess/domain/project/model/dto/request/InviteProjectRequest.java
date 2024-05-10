package org.sixback.omess.domain.project.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class InviteProjectRequest {
    @NotNull
    private final List<Long> invitedMembers;

    public InviteProjectRequest(@JsonProperty("inviteMembers") List<Long> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }
}
