package org.sixback.omess.domain.apispecification.util;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.ApiSummaryDto;
import org.sixback.omess.domain.apispecification.model.dto.CreatePathVariableRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateQueryParamRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateRequestHeaderRequest;
import org.sixback.omess.domain.apispecification.model.dto.DomainWithApiSummaryDto;
import org.sixback.omess.domain.apispecification.model.dto.response.GetApiSpecificationResponse;
import org.sixback.omess.domain.apispecification.model.entity.Api;
import org.sixback.omess.domain.apispecification.model.entity.ApiSpecification;
import org.sixback.omess.domain.apispecification.model.entity.Domain;
import org.sixback.omess.domain.apispecification.model.entity.PathVariable;
import org.sixback.omess.domain.apispecification.model.entity.QueryParam;
import org.sixback.omess.domain.apispecification.model.entity.RequestHeader;

public class ApiSpecificationMapper {
	public static RequestHeader toRequestHeader(CreateRequestHeaderRequest request, Api api){
		return new RequestHeader(request.headerKey(), request.headerValue(), api);
	}

	public static QueryParam toQueryParam(CreateQueryParamRequest request, Api api){
		return new QueryParam(request.name(), request.description(), api);
	}

	public static PathVariable toPathVariable(CreatePathVariableRequest request, Api api){
		return new PathVariable(request.name(), request.description(), api);
	}

	public static ApiSummaryDto toApiSummaryDto(Api api){
		return new ApiSummaryDto(api.getMethod(), api.getName(), api.getEndpoint(), api.getStatusCode());
	}

	public static DomainWithApiSummaryDto toDomainWithApiSummaryDto(Domain domain){
		List<ApiSummaryDto> apis = domain.getApis().stream().map(ApiSpecificationMapper::toApiSummaryDto).toList();
		return new DomainWithApiSummaryDto(domain.getName(), apis);
	}

	public static GetApiSpecificationResponse toGetApiSpecificationResponse(ApiSpecification apiSpecification){
		List<DomainWithApiSummaryDto> domains = apiSpecification.getDomains()
			.stream()
			.map(ApiSpecificationMapper::toDomainWithApiSummaryDto)
			.toList();

		return new GetApiSpecificationResponse(domains);
	}
}
