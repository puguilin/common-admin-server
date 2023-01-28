package com.github.guilin.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthTokenException extends AuthenticationException {
    public AuthTokenException(String msg) {
        super(msg);
    }
}
