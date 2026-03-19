package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 403
public class SessionLimitExceededCustomException extends RuntimeException implements HttpStatusAware {
    public SessionLimitExceededCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
