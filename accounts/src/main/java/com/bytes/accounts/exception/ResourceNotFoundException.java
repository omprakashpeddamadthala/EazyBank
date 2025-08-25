package com.bytes.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message, Object fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", message, fieldName, fieldValue));
    }
}
