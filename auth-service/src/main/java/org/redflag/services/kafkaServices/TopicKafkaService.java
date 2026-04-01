package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.redflag.exception.KafkaOperationCustomException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class TopicKafkaService {

    private final AdminClient adminClient;

    public void createTopic(String topicName, int partitions, short replicationFactor) {
        NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);
        try {
            adminClient.createTopics(List.of(topic)).all().get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaOperationCustomException("Stream interrupted while creating topic: " + topicName);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                log.warn("Topic '{}' already exists; skipping creation.", topicName);
            } else {
                log.error("Error creating topic '{}'", topicName, e.getCause());
                throw new KafkaOperationCustomException("Failed to create topic.");
            }
        }
    }

    public void deleteTopic(String topicName) {
        try {
            adminClient.deleteTopics(List.of(topicName)).all().get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaOperationCustomException("Stream interrupted while creating topic: " + topicName);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof UnknownTopicOrPartitionException) {
                log.warn("Attempt to delete a non-existent topic: '{}'", topicName);
            } else {
                log.error("Error deleting topic '{}'", topicName, e.getCause());
                throw new KafkaOperationCustomException("Failed to delete topic");
            }
        }
    }
}

