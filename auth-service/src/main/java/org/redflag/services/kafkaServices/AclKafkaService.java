package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.acl.*;
import org.apache.kafka.common.resource.*;
import org.redflag.constants.KafkaConstants;
import org.redflag.exception.KafkaOperationCustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.redflag.constants.KafkaConstants.CLUSTER_NAME;
import static org.redflag.constants.KafkaConstants.WILDCARD_HOST;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class AclKafkaService {

    private final AdminClient adminClient;
    private final GenerateKafkaNamesService namesService;

    public void createSdkConsumerAcls(String username, String topicName, String groupName) {
        String principal = namesService.buildKafkaUser(username);

        List<AclBinding> bindings = List.of(
                topicAcl(principal, topicName, PatternType.LITERAL, AclOperation.READ),
                topicAcl(principal, topicName, PatternType.LITERAL, AclOperation.DESCRIBE),
                groupAcl(principal, groupName, PatternType.LITERAL, AclOperation.READ),
                groupAcl(principal, groupName, PatternType.LITERAL, AclOperation.DESCRIBE)
        );

        applyAclCreation(bindings, username);
    }

    public void deleteSdkConsumerAcls(String username, String topicName, String groupName) {
        String principal = namesService.buildKafkaUser(username);

        List<AclBindingFilter> filters = List.of(
                aclFilter(ResourceType.TOPIC, topicName, PatternType.LITERAL, principal),
                aclFilter(ResourceType.GROUP, groupName, PatternType.LITERAL, principal)
        );

        applyAclDeletion(filters, username);
    }

    public void createMainProducerPrefixAcls(String topicPrefix) {
        String principal = namesService.buildKafkaUser(KafkaConstants.PRODUCER_NAME);

        List<AclBinding> bindings = new ArrayList<>();
        bindings.add(topicAcl(principal, topicPrefix, PatternType.PREFIXED, AclOperation.WRITE));
        bindings.add(topicAcl(principal, topicPrefix, PatternType.PREFIXED, AclOperation.DESCRIBE));
        bindings.add(clusterAcl(principal, AclOperation.IDEMPOTENT_WRITE));

        applyAclCreation(bindings, KafkaConstants.PRODUCER_NAME);
    }

    public void createMainIdempotentWriteAcl() {
        String principal = namesService.buildKafkaUser(KafkaConstants.PRODUCER_NAME);
        applyAclCreation(List.of(clusterAcl(principal, AclOperation.IDEMPOTENT_WRITE)), KafkaConstants.PRODUCER_NAME);
    }


    private void applyAclCreation(List<AclBinding> bindings, String user) {
        try {
            adminClient.createAcls(bindings).all().get();
            log.info("ACLs successfully created for user: {}", user);
        } catch (InterruptedException | ExecutionException e) {
            handleError(e, "creating an ACL for " + user);
        }
    }

    private void applyAclDeletion(List<AclBindingFilter> filters, String user) {
        try {
            adminClient.deleteAcls(filters).all().get();
            log.info("ACLs successfully removed for user: {}", user);
        } catch (InterruptedException | ExecutionException e) {
            handleError(e, "removing the ACL for " + user);
        }
    }

    private void handleError(Exception e, String context) {
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            throw new KafkaOperationCustomException("Stream interrupted at" + context);
        }

        Throwable cause = (e instanceof ExecutionException) ? e.getCause() : e;
        log.error("Error while {}: {}", context, cause.getMessage());
        throw new KafkaOperationCustomException("Kafka Security Error: " + context);
    }


    private AclBinding topicAcl(String principal, String topicName, PatternType patternType, AclOperation operation) {
        return new AclBinding(
                new ResourcePattern(ResourceType.TOPIC, topicName, patternType),
                new AccessControlEntry(principal, WILDCARD_HOST, operation, AclPermissionType.ALLOW)
        );
    }

    private AclBinding groupAcl(String principal, String groupName, PatternType patternType, AclOperation operation) {
        return new AclBinding(
                new ResourcePattern(ResourceType.GROUP, groupName, patternType),
                new AccessControlEntry(principal, WILDCARD_HOST, operation, AclPermissionType.ALLOW)
        );
    }

    private AclBinding clusterAcl(String principal, AclOperation operation) {
        return new AclBinding(
                new ResourcePattern(ResourceType.CLUSTER, CLUSTER_NAME, PatternType.LITERAL),
                new AccessControlEntry(principal, WILDCARD_HOST, operation, AclPermissionType.ALLOW)
        );
    }

    private AclBindingFilter aclFilter(ResourceType resourceType, String resourceName,
                                       PatternType patternType, String principal) {
        return new AclBindingFilter(
                new ResourcePatternFilter(resourceType, resourceName, patternType),
                new AccessControlEntryFilter(principal, WILDCARD_HOST, AclOperation.ANY, AclPermissionType.ANY)
        );
    }
}