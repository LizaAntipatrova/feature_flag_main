package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 409
public class ConflictCustomException extends RuntimeException implements HttpStatusAware {
    public ConflictCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}

