package org.redflag.services.kafka;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import org.redflag.services.kafkaServices.ScramKafkaService;

@Controller("/internal/scram")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class ScramAdminController {

    private final ScramKafkaService scramKafkaService;

    @Post
    public String create(@QueryValue String username, @QueryValue String password) throws Exception {
        scramKafkaService.createOrUpdateScramUser(username, password);
        return "scram created: " + username;
    }

    @Delete
    public String delete(@QueryValue String username) throws Exception {
        scramKafkaService.deleteScramUser(username);
        return "scram deleted: " + username;
    }
}
