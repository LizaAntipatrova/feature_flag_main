package org.redflag.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.ErrorResponse;

@Singleton
@Slf4j
public class GlobalExceptionHandler implements ExceptionHandler<Throwable, HttpResponse<ErrorResponse>> {


    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, Throwable exception) {
        ErrorCatalog error = getError(exception);

        if (error == ErrorCatalog.UNEXPECTED_ERROR) {
            log.error("Unexpected error. path = {}, method = {}, message = {}",
                    request.getPath(),
                    request.getMethod(),
                    exception.getMessage());
        } else {
            log.warn("{}. code={}, message={}, path={}, method={}",
                    error.getErrorType().getValue(),
                    error.getCode(),
                    exception.getMessage(),
                    request.getPath(),
                    request.getMethod());
        }

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
        if (exception instanceof FeatureFlagAppException appException){
            return appException.getError();
        }
        return ErrorCatalog.UNEXPECTED_ERROR;
    }


}
