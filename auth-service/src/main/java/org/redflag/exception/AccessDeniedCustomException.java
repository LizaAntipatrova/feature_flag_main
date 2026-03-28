package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 403
public class AccessDeniedCustomException extends RuntimeException implements HttpStatusAware {
    public AccessDeniedCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
