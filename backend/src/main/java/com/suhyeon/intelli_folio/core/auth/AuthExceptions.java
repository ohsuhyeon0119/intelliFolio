package com.suhyeon.intelli_folio.core.auth;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthExceptions {
    public static ResponseStatusException emailAlreadyExists() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS");
    }
    public static ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
    }
}
