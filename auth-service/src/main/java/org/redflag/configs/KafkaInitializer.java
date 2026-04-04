package org.redflag.configs;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redflag.services.kafkaServices.KafkaGeneralService;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class KafkaInitializer implements ApplicationEventListener<StartupEvent> {

    private final KafkaGeneralService kafkaGeneralService;

    @Override
    public void onApplicationEvent(StartupEvent event) {
        log.info("Starting mandatory Kafka ACL initialization...");

        kafkaGeneralService.setupMainServiceAcls();
        
        log.info("Kafka ACL initialization completed.");
    }
}