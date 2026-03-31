package org.redflag.error.handler;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.security.authentication.AuthenticationException;
import io.micronaut.security.authentication.AuthorizationException;
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler;
import jakarta.inject.Singleton;
import org.redflag.dto.ErrorResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.error.ErrorType;

@Singleton
@Primary
@Order(Ordered.HIGHEST_PRECEDENCE)
@Replaces(DefaultAuthorizationExceptionHandler.class)
public class AuthExceptionHandler implements ExceptionHandler<AuthorizationException, MutableHttpResponse<ErrorResponse>> {

    @Override
    public MutableHttpResponse<ErrorResponse> handle(HttpRequest request, AuthorizationException exception) {
        ErrorCatalog error;
        if (exception.isForbidden()) {
            error = ErrorCatalog.NO_RIGHTS_TO_OPERATION;
            ErrorResponse body = ErrorResponse.builder()
                    .code(error.getCode())
                    .errorType(error.getErrorType().getValue())
                    .message(error.getMessage())
                    .build();
            return HttpResponse.status(error.getStatus()).body(body);

        } else {
            error = ErrorCatalog.UNAUTHORIZED;
            ErrorResponse body = ErrorResponse.builder()
                    .code(error.getCode())
                    .errorType(error.getErrorType().getValue())
                    .message(error.getMessage())
                    .build();
            return HttpResponse.status(error.getStatus())
                    .body(body);
        }
    }
}
