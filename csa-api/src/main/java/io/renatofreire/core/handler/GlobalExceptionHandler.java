package io.renatofreire.core.handler;

import io.renatofreire.core.exceptions.EmailAlreadyValidatedException;
import io.renatofreire.core.exceptions.TokenAlreadyExpiredException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            EntityExistsException.class,
            EmailAlreadyValidatedException.class,
            TokenAlreadyExpiredException.class,
    })
    public ErrorResponse handleEntityExistsException(final Exception e) {
        return ErrorResponse.builder(e, HttpStatus.CONFLICT, e.getMessage()).build();
    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    public ErrorResponse handleEntityNotFoundException(final Exception e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage()).build();
    }

    @ExceptionHandler({
            IllegalStateException.class
    })
    public ErrorResponse handleIllegalStateException(final Exception e) {
        return ErrorResponse.builder(e, HttpStatus.BAD_REQUEST, e.getMessage()).build();
    }

}
