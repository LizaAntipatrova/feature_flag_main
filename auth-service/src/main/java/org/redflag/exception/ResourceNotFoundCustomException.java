package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 404
public class ResourceNotFoundCustomException extends RuntimeException implements HttpStatusAware {
    public ResourceNotFoundCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
