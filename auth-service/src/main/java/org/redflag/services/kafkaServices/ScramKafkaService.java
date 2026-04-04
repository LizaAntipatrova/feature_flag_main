package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.redflag.constants.KafkaConstants;
import org.redflag.exception.KafkaOperationCustomException;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class ScramKafkaService {

    private final AdminClient adminClient;

    public void createOrUpdateScramUser(String username, String password) {
        UserScramCredentialAlteration alteration = new UserScramCredentialUpsertion(
                username,
                new ScramCredentialInfo(ScramMechanism.SCRAM_SHA_512, KafkaConstants.DEFAULT_ITERATIONS),
                password.getBytes(StandardCharsets.UTF_8)
        );

        applyAlteration(alteration, "creation");
    }

    public void deleteScramUser(String username) {
        UserScramCredentialAlteration alteration =
                new UserScramCredentialDeletion(username, ScramMechanism.SCRAM_SHA_512);

        applyAlteration(alteration, "removal");
    }

    private void applyAlteration(UserScramCredentialAlteration alteration, String actionName) {
        try {
            adminClient.alterUserScramCredentials(List.of(alteration)).all().get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaOperationCustomException("Stream interrupted (Scram)");

        } catch (ExecutionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof ResourceNotFoundException) {
                log.warn("User not found for operation: {}", cause.getMessage());
                return;
            }

            log.error("Error {} SCRAM user", actionName, cause);
            throw new KafkaOperationCustomException("Kafka Scram Error");
        }
    }
}