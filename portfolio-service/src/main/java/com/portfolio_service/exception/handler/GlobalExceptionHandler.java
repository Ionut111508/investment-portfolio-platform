package com.portfolio_service.exception.handler;

import com.portfolio_service.error.ApiErrorResponse;
import com.portfolio_service.error.ErrorCode;
import com.portfolio_service.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ) {

        ErrorCode errorCode = ex.getErrorCode();
        log.warn(
                "Business exception [{}] on path {}: {}",
                errorCode.getCode(),
                request.getRequestURI(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiErrorResponse.from(errorCode, ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                               HttpServletRequest request) {

        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(ErrorCode.INVALID_REQUEST_PARAMETER.getHttpStatus())
                .errorCode(ErrorCode.INVALID_REQUEST_PARAMETER.getCode())
                .message(
                        "Invalid value '%s' for parameter '%s'. Expected type: %s"
                                .formatted(
                                        ex.getValue(),
                                        ex.getName(),
                                        ex.getRequiredType().getSimpleName()
                                )
                )
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error(
                "Unexpected error on path {}",
                request.getRequestURI(),
                ex
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.from(
                        ErrorCode.INTERNAL_ERROR,
                        "Unexpected internal error",
                        request
                ));
    }
}
