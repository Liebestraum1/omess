package org.sixback.omess.domain.httpmethod.model;

import java.util.List;

public record HttpHeadersResponse(
        List<String> headers
) {
}
