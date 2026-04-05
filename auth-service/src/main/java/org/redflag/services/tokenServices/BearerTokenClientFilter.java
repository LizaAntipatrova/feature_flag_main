package org.redflag.services.tokenServices;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.redflag.constants.SecurityConstants;

@Singleton
public class BearerTokenClientFilter implements HttpClientFilter {

    private final InternalTokenService internalTokenService;

    public BearerTokenClientFilter(InternalTokenService internalTokenService) {
        this.internalTokenService = internalTokenService;
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(
            MutableHttpRequest<?> request,
            ClientFilterChain chain) {

        String jwt = internalTokenService.generateInternalToken(
                SecurityConstants.AUTH_SERVICE_TOKEN_TYPE_VALUE,
                SecurityConstants.AUTH_SERVICE_TOKEN_TYPE_VALUE
        );

        request.header(HttpHeaders.AUTHORIZATION, SecurityConstants.SDK_AUTH_TOKEN_VALUE + jwt);

        return chain.proceed(request);
    }
}
