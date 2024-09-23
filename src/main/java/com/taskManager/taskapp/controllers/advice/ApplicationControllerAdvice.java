package com.taskManager.taskapp.controllers.advice;

import com.taskManager.taskapp.dto.ErrorEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@ControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public @ResponseBody ErrorEntity handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorEntity(null, e.getMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler({DataIntegrityViolationException.class, JpaSystemException.class, DuplicateKeyException.class, PersistenceException.class})
    public @ResponseBody ErrorEntity handlePersistenceException(Exception e) {
        return new ErrorEntity(null, e.getMessage());


    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody ErrorEntity handleBadException(Exception e){
        return new ErrorEntity("400", "Invalid username or password");
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorEntity handleExcepgtion(Exception e){
        return new ErrorEntity("400", e.getMessage());
    }

}
