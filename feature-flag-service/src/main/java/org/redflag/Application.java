package org.redflag;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Feature flag service",
                version = "v1",
                description = "API feature flag service"
        ),
        security = {
                @SecurityRequirement(name = "sessionAuth")
        }
)

@SecurityScheme(
        name = "sessionAuth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "SESSION"
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
