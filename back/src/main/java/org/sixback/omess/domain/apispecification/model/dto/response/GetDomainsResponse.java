package org.sixback.omess.domain.apispecification.model.dto.response;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.DomainDto;

public record GetDomainsResponse(
	List<DomainDto> domains
) {
}
