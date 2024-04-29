package org.sixback.omess.domain.apispecification.model.dto;

import java.util.List;

public record DomainWithApiSummaryDto(
	Long domainId,
	String name,
	List<ApiSummaryDto> apis
) {
	public DomainWithApiSummaryDto(Long domainId, String name, List<ApiSummaryDto> apis) {
		this.domainId = domainId;
		this.name = name;
		this.apis = List.copyOf(apis);
	}
}
