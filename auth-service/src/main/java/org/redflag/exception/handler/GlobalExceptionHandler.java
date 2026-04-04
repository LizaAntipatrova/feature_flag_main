package org.redflag.exception.handler;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.redflag.exception.HttpStatusAware;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Produces
@Singleton
@Replaces(io.micronaut.http.server.exceptions.HttpStatusHandler.class)
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        exception.printStackTrace();
        if (exception instanceof HttpStatusException httpStatusException) {
            status =  httpStatusException.getStatus();
        }

        if (exception instanceof HttpStatusAware aware) {
            status = aware.getStatus();
        }

        log.error("Error [{}] at path [{}]: {}", status.getCode(), request.getPath(), exception.getMessage());
        exception.printStackTrace();

        var response = new CustomErrorResponse(exception.getMessage(), Map.of(
                "timestamp", LocalDateTime.now(),
                "path", request.getPath()
        ));

        return HttpResponse.status(status).body(response);
    }

}