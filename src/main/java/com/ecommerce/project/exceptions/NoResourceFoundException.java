package com.ecommerce.project.exceptions;

public class NoResourceFoundException extends RuntimeException {
    private String resourceName;
    private String field;
    private String fieldName;
    private Long fieldId;

    public NoResourceFoundException() {
    }

    public NoResourceFoundException(String resourceName, String field, String fieldName) {
        super(String.format("In %s %s not found with %s", resourceName, fieldName, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public NoResourceFoundException(String resourceName, String fieldName, Long fieldId) {
        super(String.format("In %s %s not found with %d", resourceName, fieldName, fieldId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}
