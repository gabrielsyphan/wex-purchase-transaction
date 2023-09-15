package com.syphan.wexpurchasetransaction.util.handler;

import com.syphan.wexpurchasetransaction.model.dto.config.GenericExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GenericExceptionDto> entityNotFound(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(new GenericExceptionDto(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<GenericExceptionDto>> genericException(Exception exception) {
        List<FieldError> errors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
        return ResponseEntity.badRequest().body(
                errors.stream().map(GenericExceptionDto::new).collect(Collectors.toList())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericExceptionDto> runtimeException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new GenericExceptionDto(exception.getMessage()));
    }
}