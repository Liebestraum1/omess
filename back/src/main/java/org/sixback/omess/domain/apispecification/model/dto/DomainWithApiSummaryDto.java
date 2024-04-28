package org.sixback.omess.domain.apispecification.model.dto;

import java.util.List;

public record DomainWithApiSummaryDto(
	String name,
	List<ApiSummaryDto> apis
) {
	public DomainWithApiSummaryDto(String name, List<ApiSummaryDto> apis) {
		this.name = name;
		this.apis = List.copyOf(apis);
	}
}
