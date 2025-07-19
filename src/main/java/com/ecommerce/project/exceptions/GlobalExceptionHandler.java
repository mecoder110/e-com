package com.ecommerce.project.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ObjectError err;
    private ObjectError err1;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(MethodArgumentNotValidException e) {

        Map<String, String> globalException = new HashMap<>();

        e.getBindingResult().getAllErrors().stream().forEach(err -> {
            String field = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            globalException.put(field, message);
        });
        return new ResponseEntity<>(globalException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<NoResourceFoundException> myNoResourceFoundException(NoResourceFoundException e) {

        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    private ResponseEntity<APIException> myApiException(APIException e) {

        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
