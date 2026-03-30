package org.redflag.services.kafka;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;

@Singleton
@RequiredArgsConstructor
public class KafkaAdminProbeService {

    private final AdminClient adminClient;

    public String clusterId() throws Exception {
        return adminClient.describeCluster().clusterId().get();
    }
}