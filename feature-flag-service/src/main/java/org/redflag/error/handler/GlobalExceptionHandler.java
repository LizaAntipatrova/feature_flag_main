package org.redflag.error.handler;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.redflag.dto.ErrorResponse;
import org.redflag.error.ErrorCatalog;
import org.redflag.error.FeatureFlagAppException;

@Singleton
@Slf4j
public class GlobalExceptionHandler implements ExceptionHandler<Throwable, HttpResponse<ErrorResponse>> {


    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, Throwable exception) {
        ErrorCatalog error = getError(exception);

        if (error.equals(ErrorCatalog.UNEXPECTED_ERROR)){
            log.error("{}: exception: {}, message: {}",
                    error.getErrorType().getValue(),
                    exception.getClass().getName(),
                    exception.getMessage());
            exception.printStackTrace();
        }else {
            log.info("{}: code: {}, message:{}, status: {}",
                    error.getErrorType().getValue(),
                    error.getCode(),
                    error.getMessage(),
                    error.getStatus());
        }
        ErrorResponse responseBody = ErrorResponse.builder()
                .code(error.getCode())
                .errorType(error.getErrorType().getValue())
                .message(exception.getMessage())
                .build();

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
