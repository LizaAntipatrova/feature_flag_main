package org.redflag.error.handler;

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.Order;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.json.JsonSyntaxException;
import jakarta.inject.Singleton;
import org.redflag.dto.ErrorResponse;
import org.redflag.error.ErrorCatalog;

@Singleton
@Primary
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JsonSyntaxExceptionHandler implements ExceptionHandler<JsonSyntaxException, HttpResponse<ErrorResponse>> {

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, JsonSyntaxException exception) {
        ErrorCatalog error = ErrorCatalog.INVALID_JSON;
        ErrorResponse body = ErrorResponse.builder()
                .code(error.getCode())
                .errorType(error.getErrorType().getValue())
                .message(error.getMessage())
                .build();

        return HttpResponse.status(error.getStatus())
                .body(body);
    }
}
