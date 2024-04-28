package org.sixback.omess.domain.apispecification.model.dto.response;

import java.util.List;

public record GetDomainsResponse(
	List<String> domains
) {
}
