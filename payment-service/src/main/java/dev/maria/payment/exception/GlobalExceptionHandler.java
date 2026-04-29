package dev.maria.payment.exception;

import dev.maria.payment.domain.ErrorCode;
import dev.maria.payment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(), ErrorCode.ORDER_NOT_FOUND));
    }

    @ExceptionHandler
    public ErrorResponse handleUnavailable(OrderServiceUnavailableException ex) {
        return new ErrorResponse(ex.getMessage(), ErrorCode.INTERNAL_ERROR);
    }
}
