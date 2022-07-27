package com.user.usermanagement.exception;

import com.user.usermanagement.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> constraintViolationHandler(ConstraintViolationException constraintViolationException) {
        return new ResponseEntity<>(new ApiResponse<>(constraintViolationException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<String>> userExceptionHandler(UserException userException) {
        return new ResponseEntity<>(new ApiResponse<>(userException.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> genericExceptionHandler(Exception exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<String>> MissingRequestHeaderExceptionHandler(MissingRequestHeaderException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> HttpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<String>> HttpMediaTypeNotSupportedExceptionHandler(HttpMediaTypeNotSupportedException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(new ApiResponse<>(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
