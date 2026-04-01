package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.dto.CreateServiceAccessResponse;
import org.redflag.exception.KafkaOperationCustomException;

import static org.apache.kafka.common.requests.DeleteAclsResponse.log;

@Singleton
@RequiredArgsConstructor
public class KafkaGeneralService {

    private final TopicKafkaService topicKafkaService;
    private final ScramKafkaService scramKafkaService;
    private final AclKafkaService aclKafkaService;
    private final GenerateKafkaNamesService namesService;

    public CreateServiceAccessResponse setupKafka(String serviceId, String password) {
        String topicName = namesService.buildTopicName(serviceId);
        String username = namesService.buildUsername(serviceId);
        String groupName = namesService.buildGroupName(serviceId);

        boolean topicCreated = false;
        boolean userCreated = false;
        boolean aclsCreated = false;

        try {
            topicKafkaService.createTopic(topicName, 3, (short) 1);
            topicCreated = true;
            log.info("Topic created: {}", topicName);

            scramKafkaService.createOrUpdateScramUser(username, password);
            userCreated = true;
            log.info("SCRAM user created: {}", username);

            aclKafkaService.createSdkConsumerAcls(username, topicName, groupName);
            aclsCreated = true;
            log.info("ACLs configured for user {} and topic {}", username, topicName);

            return CreateServiceAccessResponse.builder()
                    .serviceId(serviceId)
                    .topic(topicName)
                    .username(username)
                    .password(password)
                    .groupName(groupName)
                    .build();

        } catch (Exception e) {
            log.error("Failed to provision service {}. Starting rollback...", serviceId, e);
            rollbackCreation(topicName, username, groupName, topicCreated, userCreated, aclsCreated);
            throw new KafkaOperationCustomException("Failed to create Kafka infrastructure: " + e.getMessage());
        }
    }


    private void rollbackCreation(String topic, String user, String group,
                                  boolean topicCreated, boolean userCreated, boolean aclsCreated) {
        if (aclsCreated) {
            aclKafkaService.deleteSdkConsumerAcls(user, topic, group);
            log.info("Rollback: ACLs deleted");
        }
        if (userCreated) {
            scramKafkaService.deleteScramUser(user);
            log.info("Rollback: User {} deleted", user);
        }
        if (topicCreated) {
            topicKafkaService.deleteTopic(topic);
            log.info("Rollback: Topic {} deleted", topic);
        }
    }


    public void cleanupKafka(String serviceId, String password) {
        String topicName = namesService.buildTopicName(serviceId);
        String username = namesService.buildUsername(serviceId);
        String groupName = namesService.buildGroupName(serviceId);

        boolean aclsDeleted = false;
        boolean userDeleted = false;
        boolean topicDeleted = false;

        try {
            aclKafkaService.deleteSdkConsumerAcls(username, topicName, groupName);
            aclsDeleted = true;
            log.info("ACLs deleted for user: {}", username);

            scramKafkaService.deleteScramUser(username);
            userDeleted = true;
            log.info("SCRAM user deleted: {}", username);

            topicKafkaService.deleteTopic(topicName);
            topicDeleted = true;
            log.info("Topic deleted: {}", topicName);

        } catch (Exception e) {
            log.error("Failed to deprovision service {}. Attempting to restore...", serviceId, e);
            rollbackDeletion(topicName, username, groupName, password,
                    aclsDeleted, userDeleted, topicDeleted);

            throw new KafkaOperationCustomException("Failed to cleanup Kafka infrastructure: " + e.getMessage());
        }
    }

    private void rollbackDeletion(String topic, String user, String group, String password,
                                  boolean aclsDeleted, boolean userDeleted, boolean topicDeleted) {
        if (topicDeleted) {
            topicKafkaService.createTopic(topic, 3, (short) 1);
            log.info("Rollback: Topic {} restored", topic);
        }
        if (userDeleted) {
            scramKafkaService.createOrUpdateScramUser(user, password);
            log.info("Rollback: User {} restored", user);
        }

        if (aclsDeleted) {
            aclKafkaService.createSdkConsumerAcls(user, topic, group);
            log.info("Rollback: ACLs restored for user {}", user);
        }
    }

    public void setupMainServiceAcls() {
        aclKafkaService.createMainProducerPrefixAcls("svc.");
        aclKafkaService.createMainIdempotentWriteAcl();

        log.info("Main service ACLs configured successfully");
    }
}
