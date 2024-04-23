package org.sixback.omess.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handlerMethodValidationExceptionHandler(
            HttpServletRequest request, HandlerMethodValidationException exception
    ) {
        printException(exception);
        String detail = exception.getAllValidationResults().toString();
        return ErrorResponse.builder(exception, BAD_REQUEST, detail)
                .type(URI.create("VALIDATION_ERROR"))
                .title("유효한 입력이 아닙니다")
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorResponse bindExceptionHandler(
            HttpServletRequest request, BindException exception
    ) {
        printException(exception);
        String detail = exception.getBindingResult().getFieldErrors().toString();
        return ErrorResponse.builder(exception, BAD_REQUEST, detail)
                .type(URI.create("VALIDATION_ERROR"))
                .title("유효한 입력이 아닙니다")
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse entityNotFoundExceptionHandler(
            HttpServletRequest request, EntityNotFoundException exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, NOT_FOUND, "요청한 data를 찾을 수 없습니다.")
                .type(URI.create("NOT_FOUND"))
                .title("data를 찾을 수 없습니다.")
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    @ExceptionHandler({NullPointerException.class, RuntimeException.class, Exception.class})
    public ErrorResponse exceptionHandler(
            HttpServletRequest request, Exception exception
    ) {
        printException(exception);
        return ErrorResponse.builder(exception, INTERNAL_SERVER_ERROR, "서버에 알 수 없는 에러가 발생여였습니다.")
                .type(URI.create("UNKNOW_ERROR"))
                .title("알 수 없는 에러")
                .instance(URI.create(request.getRequestURI()))
                .build();
    }

    private void printException(Exception exception) {
        log.error("{} 발생 - {}", exception.getClass(), exception.getMessage());
    }
}
