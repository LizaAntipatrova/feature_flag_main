package org.redflag.kafka;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

import java.util.concurrent.CompletableFuture;

@KafkaClient
public interface KafkaProducer {
    CompletableFuture<Void> sendEvent(@Topic String topic, String event);
}
