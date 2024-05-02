package org.sixback.omess.domain.apispecification.model.dto.response;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.DomainWithApiSummaryDto;

public record GetApiSpecificationResponse(
	Long apiSpecificationId,
	List<DomainWithApiSummaryDto> domains
) {
	public GetApiSpecificationResponse (
		Long apiSpecificationId, List<DomainWithApiSummaryDto> domains
	) {
		this.apiSpecificationId = apiSpecificationId;
		this.domains = List.copyOf(domains);
	}
}
