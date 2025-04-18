package com.paygrid.auth.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException(String message) {
        super(String.format(message));
    }
}