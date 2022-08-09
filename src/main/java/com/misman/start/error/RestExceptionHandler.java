package com.misman.start.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {



    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<Object> handleConflict(BadRequestException badRequestException) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, badRequestException.getErrorCode().name() ,badRequestException.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED" ,exception.getLocalizedMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
