package org.redflag.exception;

import io.micronaut.http.HttpStatus;

//500
public class KafkaOperationCustomException extends RuntimeException implements HttpStatusAware {
    public KafkaOperationCustomException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
