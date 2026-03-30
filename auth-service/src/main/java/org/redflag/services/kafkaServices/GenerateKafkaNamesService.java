package org.redflag.services.kafkaServices;

import jakarta.inject.Singleton;

@Singleton
public class GenerateKafkaNamesService {

    public String buildTopicName(String serviceId) {
        return "svc." + serviceId + ".events";
    }

    public String buildUsername(String serviceId) {
        return "sdk." + serviceId;
    }

    public String buildGroupName(String serviceId) {
        return "grp.svc." + serviceId;
    }

    public String buildKafkaUser(String username) {
        return "User:" + username;
    }

}
