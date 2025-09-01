package com.bytes.cards.controller;

import com.bytes.cards.dto.ErrorResponseDto;
import com.bytes.cards.exception.CardAlreadyExistException;
import com.bytes.cards.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CardAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleCardAlreadyExistsException(Exception exception, WebRequest request) {
        log.error("CardAlreadyExistsException: {}", exception.getMessage());
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .errorCode("CARD_ALREADY_EXISTS")
                .errorMessage(exception.getMessage())
                .errorTime( LocalDateTime.now() )
                .build();
        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .body( errorResponse );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(Exception exception, WebRequest request) {
        log.error("ResourceNotFoundException: {}", exception.getMessage());
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .errorCode("RESOURCE_NOT_FOUND")
                .errorMessage(exception.getMessage())
                .errorTime( LocalDateTime.now() )
                .build();
        return ResponseEntity
                .status( HttpStatus.NOT_FOUND )
                .body( errorResponse );
    }

    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception, WebRequest request) {
        log.error("Generic Exception: ", exception);
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .apiPath(request.getDescription(false))
                .errorCode("INTERNAL_SERVER_ERROR")
                .errorMessage("An unexpected error occurred. Please try again later.")
                .errorTime( LocalDateTime.now() )
                .build();
        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( errorResponse );
    }
}
