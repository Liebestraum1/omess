package org.sixback.omess.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;
import java.net.URI;

import static org.sixback.omess.common.exception.ErrorType.NEED_AUTHENTICATION_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse errorResponse = ErrorResponse.builder(authException, UNAUTHORIZED, NEED_AUTHENTICATION_ERROR.getTitle())
                .type(URI.create(NEED_AUTHENTICATION_ERROR.name()))
                .title(NEED_AUTHENTICATION_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorResponse.getBody());
        response.getWriter().write(jsonResponse);
    }
}
