package org.redflag.error;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    UNEXPECTED_ERROR("00-0000", ErrorType.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_EMPTY("01-0001", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST);


    private final String code;
    private final ErrorType errorType;
    private final HttpStatus status;


}
