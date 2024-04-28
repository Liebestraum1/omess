package org.sixback.omess.domain.apispecification.model.dto.response;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.DomainWithApiSummaryDto;

public record GetApiSpecificationResponse(
	List<DomainWithApiSummaryDto> domains
) {
	public GetApiSpecificationResponse (List<DomainWithApiSummaryDto> domains) {
		this.domains = List.copyOf(domains);
	}
}
