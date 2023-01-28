package com.github.guilin.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthTokenExpiredException extends AuthenticationException {
    public AuthTokenExpiredException(String msg) {
        super(msg);
    }
}
