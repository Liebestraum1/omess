package org.sixback.omess.domain.apispecification.model.dto.response;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.PathVariableDto;
import org.sixback.omess.domain.apispecification.model.dto.QueryParamDto;
import org.sixback.omess.domain.apispecification.model.dto.RequestHeaderDto;

public record GetApiResponse(
	Long apiId,
	String method,
	String name,
	String description,
	String endpoint,
	String requestSchema,
	String responseSchema,
	Short statusCode,
	List<RequestHeaderDto> requestHeaders,
	List<QueryParamDto> queryParams,
	List<PathVariableDto> pathVariables
) {
}
