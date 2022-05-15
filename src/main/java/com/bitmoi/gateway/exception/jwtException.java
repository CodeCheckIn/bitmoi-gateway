package com.bitmoi.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class jwtException extends ResponseStatusException {

    public jwtException(HttpStatus status) {
        super(HttpStatus.UNAUTHORIZED);
    }

    public jwtException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }

    public jwtException(String reason, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, reason, cause);
    }

}
