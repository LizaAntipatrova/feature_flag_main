package org.redflag.services.externalServices;

import jakarta.inject.Singleton;

import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// переделать
@Singleton
public class FeatureFlagServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(FeatureFlagServiceClient.class);

    private final Random random = new Random();

    /**
     * Заглушка вызова основного сервиса.
     * Возвращает UUID созданного департамента/организации.
     */
    public UUID createOrganizationNode(String name) {
        UUID orgUuid = UUID.randomUUID();
        LOG.info("External Service: Created organization node '{}' with UUID: {}", name, orgUuid);
        return orgUuid;
    }
}