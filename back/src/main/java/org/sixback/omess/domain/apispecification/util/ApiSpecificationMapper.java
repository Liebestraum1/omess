package org.sixback.omess.domain.apispecification.util;

import org.sixback.omess.domain.apispecification.model.dto.CreatePathVariableRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateQueryParamRequest;
import org.sixback.omess.domain.apispecification.model.dto.CreateRequestHeaderRequest;
import org.sixback.omess.domain.apispecification.model.entity.PathVariable;
import org.sixback.omess.domain.apispecification.model.entity.QueryParam;
import org.sixback.omess.domain.apispecification.model.entity.RequestHeader;

public class ApiSpecificationMapper {
	public static RequestHeader toRequestHeader(CreateRequestHeaderRequest request){
		return new RequestHeader(request.key(), request.value());
	}

	public static QueryParam toQueryParam(CreateQueryParamRequest request){
		return new QueryParam(request.name(), request.description());
	}

	public static PathVariable toPathVariable(CreatePathVariableRequest request){
		return new PathVariable(request.name(), request.description());
	}
}
