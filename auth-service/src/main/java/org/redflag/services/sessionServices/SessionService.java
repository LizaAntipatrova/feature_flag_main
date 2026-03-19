package org.redflag.services.sessionServices;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.cookie.Cookie;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.configs.properties.SessionProperties;
import org.redflag.entities.Session;
import org.redflag.entities.UiClient;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.exception.SessionLimitExceededCustomException;
import org.redflag.repositories.SessionRepository;
import org.redflag.repositories.UiClientRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class SessionService {

    private static final int MAX_SESSIONS = 5;
    public static final String COOKIE_NAME = "SESSION";

    private final SessionRepository sessionRepository;
    private final UiClientRepository uiClientRepository;
    private final SessionProperties sessionProperties;

    public Mono<Session> findActiveSession(Long sessionId) {
        return Mono.justOrEmpty(sessionRepository.findById(sessionId))
                .filter(this::isValid);
    }

    private boolean isValid(Session session) {
        return session.getTtl().isAfter(LocalDateTime.now());
    }

    public Mono<Session> createSession(String login) {
        return Mono.fromCallable(() -> {
            UiClient user = findUserOrThrow(login);
            validateSessionLimit(user.getId());
            return saveNewSession(user);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private UiClient findUserOrThrow(String login) {
        return uiClientRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundCustomException("User not found: " + login));
    }

    private void validateSessionLimit(Long userId) {
        long activeSessions = sessionRepository.countByUserIdAndTtlAfter(userId, LocalDateTime.now());
        if (activeSessions >= MAX_SESSIONS) {
            throw new SessionLimitExceededCustomException("The active sessions limit has been reached (max. "
                    + MAX_SESSIONS + ")");
        }
    }

    private Session saveNewSession(UiClient user) {
        Session session = new Session();
        session.setUser(user);
        session.setTtl(LocalDateTime.now().plusHours(sessionProperties.getTtlHours()));
        return sessionRepository.save(session);
    }

    public void invalidateSession(String sessionIdStr) {
        try {
            Long sessionId = Long.valueOf(sessionIdStr);
            sessionRepository.deleteById(sessionId);
        } catch (NumberFormatException ignored) {}
    }

    public void invalidateAllUserSessions(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }

    public HttpResponse<?> buildSuccessResponse(Session session) {
        final long SESSION_MAX_AGE = sessionProperties.getTtlHours() * 3600;

        return HttpResponse.ok(Map.of("status", "success"))
                .cookie(createSessionCookie(session.getId(), SESSION_MAX_AGE));
    }

    public Cookie createSessionCookie(Object value, long maxAge) {
        return Cookie.of(COOKIE_NAME, String.valueOf(value))
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge);
    }

}