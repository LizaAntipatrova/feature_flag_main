package org.redflag.services.kafka;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import lombok.RequiredArgsConstructor;
import org.redflag.services.kafkaServices.TopicKafkaService;

@Controller("/internal/topics")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class TopicAdminController {

    private final TopicKafkaService topicKafkaService;

    @Post
    public String create(@QueryValue String name) throws Exception {
        topicKafkaService.createTopic(name, 3, (short) 1);
        return "created: " + name;
    }

    @Delete
    public String delete(@QueryValue String name) throws Exception {
        topicKafkaService.deleteTopic(name);
        return "deleted: " + name;
    }
}