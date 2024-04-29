package org.sixback.omess.domain.apispecification.model.dto;

public record RequestHeaderDto(
	String headerKey,
	String headerValue
) {
}
