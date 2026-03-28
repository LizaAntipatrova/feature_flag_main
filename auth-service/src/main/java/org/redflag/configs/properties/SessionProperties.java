package org.redflag.configs.properties;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties("micronaut.security.session")
@Data
public class SessionProperties {

    private Long ttlHours;

    private Long maxSession;


}
