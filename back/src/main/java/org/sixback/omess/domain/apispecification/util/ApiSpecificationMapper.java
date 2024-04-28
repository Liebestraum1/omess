package org.sixback.omess.domain.apispecification.util;

import org.sixback.omess.domain.apispecification.model.dto.CreatePathVariableRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateQueryParamRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateRequestHeaderRequest;
import org.sixback.omess.domain.apispecification.model.entity.Api;
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
}
