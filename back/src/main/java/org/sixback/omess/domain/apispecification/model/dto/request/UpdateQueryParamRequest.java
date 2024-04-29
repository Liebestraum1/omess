package org.sixback.omess.domain.apispecification.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateQueryParamRequest(
	@NotEmpty
	@Size(min = 1, max = 20)
	String name,

	@Size(min = 1, max = 50)
	String description
){
	public UpdateQueryParamRequest(String name){
		this(name, null);
	}
}
