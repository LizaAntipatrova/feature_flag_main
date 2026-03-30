//package org.redflag.services.kafka;
//
//import io.micronaut.context.annotation.Factory;
//import jakarta.inject.Singleton;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.admin.AdminClientConfig;
//
//import java.util.Properties;
//
//@Factory
//public class KafkaAdminFactory {
//
//    @Singleton
//    public AdminClient adminClient(KafkaAdminConfiguration configuration) {
//        Properties props = new Properties();
//
//        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getBootstrapServers());
//        props.put("security.protocol", configuration.getSecurityProtocol());
//        props.put("sasl.mechanism", configuration.getSaslMechanism());
//        props.put(
//                "sasl.jaas.config",
//                "org.apache.kafka.common.security.scram.ScramLoginModule required " +
//                        "username=\"" + configuration.getUsername() + "\" " +
//                        "password=\"" + configuration.getPassword() + "\";"
//        );
//
//        return AdminClient.create(props);
//    }
//}