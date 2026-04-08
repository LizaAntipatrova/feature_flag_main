package org.redflag.kafka;

import jakarta.inject.Singleton;

import java.util.UUID;

@Singleton
public class KafkaTopicNameUtil {

    public static final String TOPIC_PREFIX = "svc.";
    public static final String TOPIC_POSTFIX = ".events";

    public String getTopicNameByNodeUuid(UUID uuid){
        return TOPIC_PREFIX + uuid.toString() + TOPIC_POSTFIX;
    }
}
