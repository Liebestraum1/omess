package org.sixback.omess.common.exception;

import static org.sixback.omess.common.exception.ErrorType.*;
import static org.sixback.omess.domain.apispecification.exception.ApiSpecificationErrorMessage.*;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

import org.sixback.omess.domain.apispecification.exception.InvalidApiInputException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableExceptionException(
            HttpServletRequest request, HttpMessageNotReadableException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, BAD_REQUEST, INCOMPLETE_REQUEST_BODY_ERROR.getTitle())
                .type(URI.create(INCOMPLETE_REQUEST_BODY_ERROR.name()))
                .title(INCOMPLETE_REQUEST_BODY_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorResponse missingServletRequestParameterException(
            HttpServletRequest request, MissingServletRequestParameterException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, BAD_REQUEST, "parameter: " + exception.getParameterName() + " 은 필수 값입니다.")
                .type(URI.create(VALIDATION_ERROR.name()))
                .title(VALIDATION_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handlerMethodValidationExceptionHandler(
            HttpServletRequest request, HandlerMethodValidationException exception
    ) {
        printException(exception);
        String detail = exception.getAllValidationResults().toString();
        return ErrorResponse.builder(exception, BAD_REQUEST, detail)
                .type(URI.create(VALIDATION_ERROR.name()))
                .title(VALIDATION_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorResponse bindExceptionHandler(
            HttpServletRequest request, BindException exception
    ) {
        printException(exception);
        List<FieldErrorMessage> detail = exception.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldErrorMessage::new)
                .toList();
        return ErrorResponse.builder(exception, BAD_REQUEST, detail.toString())
                .type(URI.create(VALIDATION_ERROR.name()))
                .title(VALIDATION_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ErrorResponse unAuthenticationExceptionHandler(
            HttpServletRequest request, InsufficientAuthenticationException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, UNAUTHORIZED, UNAUTHENTICATED_ERROR.getTitle())
                .type(URI.create(UNAUTHENTICATED_ERROR.name()))
                .title(UNAUTHENTICATED_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse entityNotFoundExceptionHandler(
            HttpServletRequest request, EntityNotFoundException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, NOT_FOUND, NOT_FOUND_ERROR.getTitle())
                .type(URI.create(NOT_FOUND_ERROR.name()))
                .title(NOT_FOUND_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(InvalidApiInputException.class)
    public ErrorResponse invalidJsonSchemaExceptionHandler(
        HttpServletRequest request, InvalidApiInputException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, BAD_REQUEST, INVALID_JSON_SCHEMA.getMessage())
            .type(URI.create(INVALID_JSON_SCHEMA.name()))
            .title(INVALID_JSON_SCHEMA.getMessage())
            .instance(URI.create(request.getRequestURI()))
            .build();
    }

    @ExceptionHandler({NullPointerException.class, RuntimeException.class, Exception.class})
    public ErrorResponse exceptionHandler(
            HttpServletRequest request, Exception exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, INTERNAL_SERVER_ERROR, "서버에 알 수 없는 에러가 발생여였습니다.")
                .type(URI.create(UNKNOWN_ERROR.name()))
                .title(UNKNOWN_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    private void printException(Exception exception) {
        log.error("{} 발생: {}", exception.getClass(), exception.getMessage());
    }
}
