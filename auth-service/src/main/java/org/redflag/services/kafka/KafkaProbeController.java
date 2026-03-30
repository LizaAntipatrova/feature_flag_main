package org.redflag.services.kafka;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;

@Controller("/internal/kafka")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class KafkaProbeController {

    private final KafkaAdminProbeService probeService;

    @Get("/ping")
    public String ping() throws Exception {
        return "clusterId=" + probeService.clusterId();
    }
}