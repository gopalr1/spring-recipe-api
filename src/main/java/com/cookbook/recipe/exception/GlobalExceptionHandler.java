package com.cookbook.recipe.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gopal_re
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * This method handles ResourceNotFoundException as 404
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleResourceNotFoundException(final ResourceNotFoundException exception) {

        log.error("ResourceNotFoundException occurred, Error message : {}", exception.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * This method handles MethodArgumentNotValidException as 400
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("Validation error occurred");
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream().map(this::prepareError).collect(Collectors.toList());
        return new ResponseEntity<>(new ErrorDetails(new Date(), errorList), HttpStatus.BAD_REQUEST);
    }

    private String prepareError(FieldError e) {
        return e.getField() + ":" + e.getDefaultMessage();
    }


    /**
     * Handle Exception. @ExceptionHandler(Exception.class) indicates that this
     * method would handle exceptions of the Exception type.
     *
     * @param ex
     * @param
     * @return
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleException(Exception ex) {
        log.error("Unknown Error occurred, Error message : {}", ex);
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
