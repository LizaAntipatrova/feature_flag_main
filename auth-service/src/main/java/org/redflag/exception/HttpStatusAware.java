package org.redflag.exception;

import io.micronaut.http.HttpStatus;

public interface HttpStatusAware {
    HttpStatus getStatus();
}