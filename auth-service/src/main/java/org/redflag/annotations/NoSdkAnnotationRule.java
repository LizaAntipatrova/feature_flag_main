package org.redflag.annotations;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.rules.SecurityRuleResult;
import io.micronaut.web.router.MethodBasedRouteMatch;
import io.micronaut.web.router.RouteMatch;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import org.redflag.constants.SecurityConstants;
import reactor.core.publisher.Mono;

@Singleton
public class NoSdkAnnotationRule implements SecurityRule<HttpRequest<?>>, Ordered {

//    private static final String ROUTE_MATCH_ATTRIBUTE = "micronaut.http.route.match";

    @Override
    public Publisher<SecurityRuleResult> check(HttpRequest<?> request, @Nullable Authentication auth) {
        return Mono.just(request.getAttribute(SecurityConstants.ROUTE_MATCH_ATTRIBUTE, RouteMatch.class)
                .filter(match -> match instanceof MethodBasedRouteMatch)
                .map(match -> (MethodBasedRouteMatch<?, ?>) match)
                .map(methodRoute -> {
                    boolean hasNoSdk = methodRoute.hasAnnotation(NoSdkAllowed.class) ||
                            methodRoute.getDeclaringType().isAnnotationPresent(NoSdkAllowed.class);
                    if (!hasNoSdk) {
                        return SecurityRuleResult.UNKNOWN;
                    }
                    if (auth == null) {
                        return SecurityRuleResult.UNKNOWN;
                    }
                    Object typeAttr = auth.getAttributes().get("type");
                    String clientType = (typeAttr != null) ? typeAttr.toString() : "";

                    if ("sdk_client".equals(clientType)) {
                        return SecurityRuleResult.REJECTED;
                    }

                    return SecurityRuleResult.UNKNOWN;
                })
                .orElse(SecurityRuleResult.UNKNOWN));
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

}