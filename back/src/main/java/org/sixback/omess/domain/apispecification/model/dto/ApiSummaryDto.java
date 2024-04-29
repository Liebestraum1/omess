package org.sixback.omess.domain.apispecification.model.dto;

public record ApiSummaryDto(
	Long  apiId,
	String method,
	String name,
	String endpoint,
	Short statusCode
) {
}
