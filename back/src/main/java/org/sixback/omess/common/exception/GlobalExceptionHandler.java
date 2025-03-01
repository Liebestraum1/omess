package org.sixback.omess.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.util.List;

import static org.sixback.omess.common.exception.ErrorType.*;
import static org.sixback.omess.domain.apispecification.exception.ApiSpecificationErrorMessage.INVALID_JSON_SCHEMA;
import static org.sixback.omess.domain.file.model.enums.FileErrorMessage.MAX_UPLOAD_SIZE_EXCEEDED_ERROR;
import static org.springframework.http.HttpStatus.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableExceptionException(
            HttpServletRequest request, HttpMessageNotReadableException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, BAD_REQUEST, INCOMPLETE_REQUEST_BODY_ERROR.getTitle())
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
                .title(VALIDATION_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorResponse maxUploadSizeExceededExceptionHandler(
            HttpServletRequest request,
            MaxUploadSizeExceededException e
    ) {
        return ErrorResponse.builder(e, BAD_REQUEST, MAX_UPLOAD_SIZE_EXCEEDED_ERROR.getMessage())
                .title(MAX_UPLOAD_SIZE_EXCEEDED_ERROR.name())
                .instance(URI.create(request.getRequestURI()))
                .build()
                ;
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ErrorResponse unAuthenticationExceptionHandler(
            HttpServletRequest request, InsufficientAuthenticationException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, UNAUTHORIZED, UNAUTHENTICATED_ERROR.getTitle())
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
                .title(INVALID_JSON_SCHEMA.getMessage())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler({NullPointerException.class, RuntimeException.class, Exception.class})
    public ErrorResponse exceptionHandler(
            HttpServletRequest request, Exception exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, INTERNAL_SERVER_ERROR, UNKNOWN_ERROR.getTitle())
                .title(UNKNOWN_ERROR.getTitle())
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    private void printException(Exception exception) {
        log.error("{} 발생: {}", exception.getClass(), exception.getMessage());
    }
}
