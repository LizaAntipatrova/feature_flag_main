package org.redflag.exception;

import io.micronaut.http.HttpStatus;

// 500
public class ServerCustomException extends RuntimeException implements HttpStatusAware{
    public ServerCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
