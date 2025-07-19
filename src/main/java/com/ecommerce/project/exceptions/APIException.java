package com.ecommerce.project.exceptions;

public class APIException extends RuntimeException {

    private String resourceName;
    private String field;
    private Long fieldId;

    public APIException(String message) {
        super(message);
    }
}
