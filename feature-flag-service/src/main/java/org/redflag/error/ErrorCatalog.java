package org.redflag.error;

import io.micronaut.http.HttpStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCatalog {
    UNEXPECTED_ERROR("00-0000", null, ErrorType.UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),

    NOT_EMPTY("01-0001", "Поле %s не может быть пустым или null", ErrorType.CLIENT_ERROR, HttpStatus.BAD_REQUEST),

    NOT_UNIQUE_ORGANIZATION_NAME("02-0003", "Организация с таким именем уже существует", ErrorType.BUSINESS_ERROR, HttpStatus.CONFLICT);


    private final String code;
    private final String message;
    private final ErrorType errorType;
    private final HttpStatus status;

    public FeatureFlagAppException withMessageArgs(Object... messageArgs) {
        return new FeatureFlagAppException(this, this.message.formatted(messageArgs));
    }
    public FeatureFlagAppException getException() {
        return new FeatureFlagAppException(this, this.message);
    }


}
