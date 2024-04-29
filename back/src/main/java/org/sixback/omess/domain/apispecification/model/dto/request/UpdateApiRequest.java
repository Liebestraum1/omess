package org.sixback.omess.domain.apispecification.model.dto.request;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UpdateApiRequest {
	@NotEmpty
	@Size(min = 1, max = 10)
	private String method;

	@NotEmpty
	@Size(min = 1, max = 90)
	private String name;

	@Size(min = 1, max = 50)
	private String description;

	@NotEmpty
	@Size(min = 1, max = 2000)
	private String endpoint;

	@Min(100)
	@Max(599)
	@NotNull
	private Short statusCode;

	private String requestSchema;

	private String responseSchema;

	private List<UpdateRequestHeaderRequest> updateRequestHeaderRequests;
	private List<UpdateQueryParamRequest> updateQueryParamRequests;
	private List<UpdatePathVariableRequest> updatePathVariableRequests;
}

