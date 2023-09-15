package com.syphan.wexpurchasetransaction.model.dto.config;

import org.springframework.validation.FieldError;

public class GenericExceptionDto {
    private String message;

    public GenericExceptionDto(FieldError fieldError) {
        this.message = fieldError.getDefaultMessage();
    }

    public GenericExceptionDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}