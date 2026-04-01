package org.redflag.constants;

public final class KafkaConstants {

    public static final Integer DEFAULT_ITERATIONS = 4096;
    public static final String WILDCARD_HOST = "*";
    public static final String CLUSTER_NAME = "kafka-cluster";
    public static final String PRODUCER_NAME = "main_service";

    public static final String FIRST_PART_TOPIC_NAME = "svc.";
    public static final String LAST_PART_TOPIC_NAME = ".events";
    public static final String USERNAME_PART_NAME = "sdk.";
    public static final String GROUP_PART_NAME = "grp.svc.";
    public static final String KAFKA_USER_TYPE = "User:";

}
