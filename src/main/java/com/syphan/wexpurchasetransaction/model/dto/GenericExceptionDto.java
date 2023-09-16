package com.syphan.wexpurchasetransaction.model.dto;

import org.springframework.validation.FieldError;

public record GenericExceptionDto(String message) {
    public GenericExceptionDto(FieldError fieldError) {
        this(fieldError.getDefaultMessage());
    }
}