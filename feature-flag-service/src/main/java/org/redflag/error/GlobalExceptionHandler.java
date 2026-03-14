package org.redflag.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.ErrorResponse;

@Singleton
@Slf4j
public class GlobalExceptionHandler implements ExceptionHandler<Throwable, HttpResponse<ErrorResponse>> {


    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, Throwable exception) {
        ErrorCatalog error = getError(exception);

        exception.printStackTrace();

        ErrorResponse responseBody = new ErrorResponse(
                error.getCode(),
                error.getErrorType().getValue(),
                exception.getMessage()
        );

        return HttpResponse
                .status(error.getStatus())
                .body(responseBody);

    }

    private ErrorCatalog getError(Throwable exception) {
        if (exception instanceof FeatureFlagAppException appException) {
            return appException.getError();
        }
        return ErrorCatalog.UNEXPECTED_ERROR;
    }


}
