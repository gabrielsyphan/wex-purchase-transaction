package com.syphan.wexpurchasetransaction.util.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.syphan.wexpurchasetransaction.model.dto.GenericExceptionDto;
import com.syphan.wexpurchasetransaction.util.exception.InvalidTransactionException;
import com.syphan.wexpurchasetransaction.util.exception.InvalidUserException;
import com.syphan.wexpurchasetransaction.util.exception.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler({InvalidTransactionException.class, InvalidUserException.class, EntityNotFoundException.class})
    public ResponseEntity<GenericExceptionDto> invalidTransaction(RuntimeException exception) {
        return ResponseEntity.badRequest().body(new GenericExceptionDto(exception.getMessage()));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<GenericExceptionDto> transactionNotFound(TransactionNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericExceptionDto(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<GenericExceptionDto>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        return ResponseEntity.badRequest().body(
                errors.stream().map(GenericExceptionDto::new).collect(Collectors.toList())
        );
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

    @ExceptionHandler({JWTVerificationException.class, TokenExpiredException.class})
    public ResponseEntity<GenericExceptionDto> jwtVerificationException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericExceptionDto(exception.getMessage()));
    }

    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    public ResponseEntity<String> handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException ex) {
        // Aqui você pode personalizar a mensagem de erro que deseja retornar para o usuário
        String errorMessage = "Ocorreu um erro ao processar a solicitação.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}