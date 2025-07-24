package com.tecnova.technical_test.infrastructure.rest.advice;

import com.tecnova.technical_test.domain.model.dto.response.ErrorResponse;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.AuthException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.ResourceNotFoundException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.TaskStatusException;
import com.tecnova.technical_test.infrastructure.adapter.exceptions.UserException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final String EXCEPTION_MESSAGE_CAUSE = "Exception Message {} Caused By {}";

    Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleEmptyInput(UserException ex, WebRequest request) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.toString(),
                "User not Found", ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<ErrorResponse> handleTaskExceptions(TaskException ex, WebRequest request) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.toString(),
                "Task not Found", ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskStatusException.class)
    public ResponseEntity<ErrorResponse> handleTaskStatisExceptions(TaskStatusException ex, WebRequest request) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.NOT_FOUND.toString(),
                "TaskStatus not Found", ex.getErrorMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleParameterException(MethodArgumentNotValidException ex, WebRequest request) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String detail = String.join("; ", fieldErrors);

        ErrorResponse errorResponse = buildErrorResponse(HttpStatus.BAD_REQUEST.toString(),
                "One or more parameters are required in the Request.",
                detail, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        Map<String, String> error = new HashMap<>();
        error.put("error", "Resource not found");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid petition");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex) {
        logger.info(EXCEPTION_MESSAGE_CAUSE, ex.getMessage(), ex.getCause());

        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Malformed JSON or invalid field format");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Database constraint violation: " + ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409 Conflict es apropiado
    }

    private ErrorResponse buildErrorResponse(String code, String message, String details, WebRequest request) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .details(details)
                .timestamp(LocalDateTime.now().toString())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
    }
}
