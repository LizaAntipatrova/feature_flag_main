package org.redflag.services.logging;

import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.json.JsonMapper;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.security.authentication.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.redflag.constants.LoggingConstants.*;
import static org.redflag.constants.SecurityConstants.UI_CLIENT_ID_NAME;

@Slf4j
@Filter("/**")
@RequiredArgsConstructor
public class LoggingFilter implements HttpServerFilter {

    private final JsonMapper jsonMapper;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        long startTime = System.currentTimeMillis();

        return Flux.from(chain.proceed(request))
                .doOnNext(res -> processLog(request, res, startTime))
                .onErrorResume(throwable -> {
                    processLog(request, null, startTime);
                    return Flux.error(throwable);
                });
    }

    private void processLog(HttpRequest<?> request, MutableHttpResponse<?> response, long startTime) {
        String userId = request.getUserPrincipal(Authentication.class)
                .map(auth -> String.valueOf(auth.getAttributes().get(UI_CLIENT_ID_NAME)))
                .orElse(GUEST_NO_ID);

        long duration = System.currentTimeMillis() - startTime;
        logRequest(request, response, duration, userId);
    }

    private void logRequest(HttpRequest<?> request, MutableHttpResponse<?> response, long duration, String userId) {
        String method = request.getMethod().name();
        String path = request.getPath();

        int status = (response != null) ? response.getStatus().getCode() : 500;

        Object body = request.getBody().orElse(null);
        String maskedBody = (body != null) ? getMaskedJson(body) : EMPTY_BODY;

        log.info(LOG_PATTERN, userId, method, path, status, duration, maskedBody);
    }

    private String getMaskedJson(Object body) {
        try {
            if (body instanceof String) {
                body = jsonMapper.readValue((String) body, Map.class);
            }
            JsonNode node = jsonMapper.writeValueToTree(body);
            Map<String, Object> map = new HashMap<>(jsonMapper.readValueFromTree(node, Map.class));
            maskMapRecursive(map);
            return jsonMapper.writeValueAsString(map);
        } catch (Exception e) {
            return COMPLEX_BODY_FALLBACK;
        }
    }

    @SuppressWarnings("info")
    private void maskMapRecursive(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (MASKED_FIELDS.contains(entry.getKey().toLowerCase())) {
                entry.setValue(SENSITIVE_DATA_MASK);
            } else if (entry.getValue() instanceof Map) {
                maskMapRecursive((Map<String, Object>) entry.getValue());
            } else if (entry.getValue() instanceof List) {
                for (Object item : (List<?>) entry.getValue()) {
                    if (item instanceof Map) maskMapRecursive((Map<String, Object>) item);
                }
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}