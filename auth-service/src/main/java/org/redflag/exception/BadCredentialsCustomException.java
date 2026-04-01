package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 400
public class BadCredentialsCustomException extends RuntimeException implements HttpStatusAware {

    public BadCredentialsCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}