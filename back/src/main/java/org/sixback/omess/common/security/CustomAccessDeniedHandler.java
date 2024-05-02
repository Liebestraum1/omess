package org.sixback.omess.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.net.URI;

import static org.sixback.omess.common.exception.ErrorType.UNAUTHORIZED_ERROR;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder(accessDeniedException, FORBIDDEN, UNAUTHORIZED_ERROR.getTitle())
                .type(URI.create(UNAUTHORIZED_ERROR.name()))
                .title(UNAUTHORIZED_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
        response.getWriter().write(jsonResponse);
    }
}
