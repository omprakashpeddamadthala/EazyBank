package com.bytes.loans.controller;

import com.bytes.loans.dto.ErrorResponseDto;
import com.bytes.loans.dto.ResponseDto;
import com.bytes.loans.exception.LoanAlreadyExistException;
import com.bytes.loans.exception.ResourceNotFoundException;
import lombok.Data;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error( "Method argument not valid exception: ", exception );
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = exception.getBindingResult().getAllErrors();

        validationErrorList.forEach( (error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put( fieldName, validationMsg );
        } );
        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST )
                .body( ErrorResponseDto.builder()
                        .apiPath( request.getDescription( Boolean.FALSE ) )
                        .errorCode( HttpStatus.BAD_REQUEST.toString() )
                        .errorMessage( validationErrors.toString() )
                        .errorTime( LocalDateTime.now() )
                        .build() );
    }

    @ExceptionHandler(LoanAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleLoanAlreadyExistException(LoanAlreadyExistException exception, WebRequest request){
        log.error("Loan already exist exception: ", exception);
        return ResponseEntity
                .status( HttpStatus.BAD_REQUEST)
                .body( ErrorResponseDto.builder()
                        .apiPath(request.getDescription(Boolean.FALSE))
                        .errorCode( HttpStatus.BAD_REQUEST.toString() )
                        .errorMessage( exception.getMessage() )
                        .errorTime( LocalDateTime.now() )
                        .build() );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        log.error( "Resource not found: ", exception );
        return ResponseEntity
                .status( HttpStatus.NOT_FOUND )
                .body( ErrorResponseDto.builder()
                        .apiPath( request.getDescription( Boolean.FALSE ) )
                        .errorCode( HttpStatus.NOT_FOUND.toString() )
                        .errorMessage( exception.getMessage() )
                        .errorTime( LocalDateTime.now() )
                        .build() );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception,WebRequest request){
        log.error( "Internal server error: ", exception );
        return ResponseEntity
                .status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( ErrorResponseDto.builder()
                        .apiPath( request.getDescription( Boolean.FALSE ) )
                        .errorCode( HttpStatus.INTERNAL_SERVER_ERROR.toString() )
                        .errorMessage( exception.getMessage() )
                        .errorTime( LocalDateTime.now() )
                        .build() );
    }
}
