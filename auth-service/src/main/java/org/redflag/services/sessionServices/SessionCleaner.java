package org.redflag.services.sessionServices;

import io.micronaut.scheduling.annotation.Scheduled;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.redflag.constants.SecurityConstants;
import org.redflag.repositories.SessionRepository;

import java.time.LocalDateTime;

@Singleton
@AllArgsConstructor
public class SessionCleaner {

//    private static final String CLEANUP_INTERVAL = "${micronaut.security.session.cleanup-interval}";

    private final SessionRepository sessionRepository;

    @Scheduled(fixedDelay = SecurityConstants.CLEANUP_INTERVAL)
    @Transactional
    public void cleanExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        sessionRepository.deleteByTtlBefore(now);
    }
}