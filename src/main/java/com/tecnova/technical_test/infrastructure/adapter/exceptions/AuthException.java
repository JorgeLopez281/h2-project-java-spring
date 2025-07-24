package com.tecnova.technical_test.infrastructure.adapter.exceptions;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
