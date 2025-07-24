package com.tecnova.technical_test.infrastructure.adapter.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatus errorCode;
    private String errorMessage;
}
