package com.bytes.accounts.controller;

import com.bytes.accounts.dto.ErrorResponseDto;
import com.bytes.accounts.exception.CustomerAlreadyExistException;
import com.bytes.accounts.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body( ErrorResponseDto.builder()
                        .apiPath(webRequest.getDescription(false))
                        .errorMessage(exception.getMessage())
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .errorTime(java.time.LocalDateTime.now())
                        .build()
                );
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistException exception, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body( ErrorResponseDto.builder()
                        .apiPath(webRequest.getDescription(false))
                        .errorMessage(exception.getMessage())
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .errorTime(java.time.LocalDateTime.now())
                        .build()
        );

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body( ErrorResponseDto.builder()
                        .apiPath(webRequest.getDescription(false))
                        .errorMessage(exception.getMessage())
                        .errorCode(HttpStatus.NOT_FOUND.toString())
                        .errorTime(java.time.LocalDateTime.now())
                        .build()
        );

    }
}
