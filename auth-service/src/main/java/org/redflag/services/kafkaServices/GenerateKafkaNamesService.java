package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;

import static org.redflag.constants.KafkaConstants.*;

@Singleton
public class GenerateKafkaNamesService {

    public String buildTopicName(String serviceId) {
        return FIRST_PART_TOPIC_NAME + serviceId + LAST_PART_TOPIC_NAME;
    }


    public String buildUsername(String serviceId) {
        return USERNAME_PART_NAME + serviceId;
    }

    public String buildGroupName(String serviceId) {
        return GROUP_PART_NAME + serviceId;
    }

    public String buildKafkaUser(String username) {
        return KAFKA_USER_TYPE + username;
    }

}
