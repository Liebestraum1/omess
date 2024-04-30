package org.sixback.omess.domain.project.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@ToString
public class UpdateProjectRequest {
    @NotBlank
    @Length(max = 10)
    private final String name;

    public UpdateProjectRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
}
