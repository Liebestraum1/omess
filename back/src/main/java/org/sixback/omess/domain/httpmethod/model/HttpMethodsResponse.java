package org.sixback.omess.domain.httpmethod.model;

import java.util.List;

public record HttpMethodsResponse(
        List<String> methods
) {
}
