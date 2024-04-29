package org.sixback.omess.domain.apispecification.util;

import java.util.List;

import org.sixback.omess.domain.apispecification.model.dto.ApiSummaryDto;
import org.sixback.omess.domain.apispecification.model.dto.DomainDto;
import org.sixback.omess.domain.apispecification.model.dto.DomainWithApiSummaryDto;
import org.sixback.omess.domain.apispecification.model.dto.PathVariableDto;
import org.sixback.omess.domain.apispecification.model.dto.QueryParamDto;
import org.sixback.omess.domain.apispecification.model.dto.RequestHeaderDto;
import org.sixback.omess.domain.apispecification.model.dto.request.CreatePathVariableRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateQueryParamRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.CreateRequestHeaderRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.UpdatePathVariableRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.UpdateQueryParamRequest;
import org.sixback.omess.domain.apispecification.model.dto.request.UpdateRequestHeaderRequest;
import org.sixback.omess.domain.apispecification.model.dto.response.GetApiResponse;
import org.sixback.omess.domain.apispecification.model.dto.response.GetApiSpecificationResponse;
import org.sixback.omess.domain.apispecification.model.dto.response.GetDomainsResponse;
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

	public static RequestHeader toRequestHeader(UpdateRequestHeaderRequest request, Api api){
		return new RequestHeader(request.headerKey(), request.headerValue(), api);
	}

public static QueryParam toQueryParam(UpdateQueryParamRequest request, Api api){
		return new QueryParam(request.name(), request.description(), api);
	}

	public static PathVariable toPathVariable(UpdatePathVariableRequest request, Api api){
		return new PathVariable(request.name(), request.description(), api);
	}

	public static ApiSummaryDto toApiSummaryDto(Api api){
		return new ApiSummaryDto(api.getId(), api.getMethod(), api.getName(), api.getEndpoint(), api.getStatusCode());
	}

	public static DomainWithApiSummaryDto toDomainWithApiSummaryDto(Domain domain){
		List<ApiSummaryDto> apis = domain.getApis().stream().map(ApiSpecificationMapper::toApiSummaryDto).toList();
		return new DomainWithApiSummaryDto(domain.getId(), domain.getName(), apis);
	}

	public static GetApiSpecificationResponse toGetApiSpecificationResponse(ApiSpecification apiSpecification){
		List<DomainWithApiSummaryDto> domains = apiSpecification.getDomains()
			.stream()
			.map(ApiSpecificationMapper::toDomainWithApiSummaryDto)
			.toList();

		return new GetApiSpecificationResponse(domains);
	}

	public static DomainDto toDomainDto(Domain domain){
		return new DomainDto(domain.getId(), domain.getName());
	}

	public static GetDomainsResponse toGetDomainsResponse(List<Domain> domains){
		 return new GetDomainsResponse(domains.stream().map(ApiSpecificationMapper::toDomainDto).toList());
	}

	public static RequestHeaderDto toRequestHeaderDto(RequestHeader requestHeader){
		return new RequestHeaderDto(requestHeader.getHeaderKey(), requestHeader.getHeaderValue());
	}

	public static QueryParamDto toQueryParamDto(QueryParam queryParam){
		return new QueryParamDto(queryParam.getName(), queryParam.getDescription());
	}

	public static PathVariableDto toPathVariableDto(PathVariable pathVariable){
		return new PathVariableDto(pathVariable.getName(), pathVariable.getDescription());
	}

	public static GetApiResponse toGetApiResponse(Api api){
		List<RequestHeaderDto> requestHeaders = api.getRequestHeaders()
			.stream()
			.map(ApiSpecificationMapper::toRequestHeaderDto)
			.toList();

		List<QueryParamDto> queryParams = api.getQueryParams()
			.stream()
			.map(ApiSpecificationMapper::toQueryParamDto)
			.toList();

		List<PathVariableDto> pathVariables = api.getPathVariables()
			.stream()
			.map(ApiSpecificationMapper::toPathVariableDto)
			.toList();

		return new GetApiResponse(
			api.getId(), api.getMethod(), api.getName(), api.getDescription(), api.getEndpoint(),
			api.getRequestSchema(), api.getResponseSchema(),
			api.getStatusCode(), requestHeaders, queryParams, pathVariables
		);
	}
}
