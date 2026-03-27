package org.redflag.services.sessionServices;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.cookie.Cookie;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.redflag.configs.properties.SessionProperties;
import org.redflag.constants.SecurityConstants;
import org.redflag.dto.SessionResponseDto;
import org.redflag.entities.Session;
import org.redflag.entities.UiClient;
import org.redflag.exception.BadCredentialsCustomException;
import org.redflag.exception.ResourceNotFoundCustomException;
import org.redflag.exception.AccessDeniedCustomException;
import org.redflag.repositories.SessionRepository;
import org.redflag.repositories.UiClientRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.Map;

@Singleton
@RequiredArgsConstructor
public class SessionService {

//    public static final String COOKIE_NAME = "SESSION";
//    public static final int SECONDS_IN_HOUR = 3600;

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
        if (activeSessions >= sessionProperties.getMaxSession()) {
            throw new AccessDeniedCustomException("The active sessions limit has been reached (max. "
                    + sessionProperties.getMaxSession() + ")");
        }
    }

    private Session saveNewSession(UiClient user) {
        Session session = new Session();
        session.setUser(user);
        session.setTtl(LocalDateTime.now().plusHours(sessionProperties.getTtlHours()));
        return sessionRepository.save(session);
    }

    public void invalidateSession(String sessionIdStr, String currentUserLogin) {
//        long sessionId;
//        try {
//            sessionId = Long.parseLong(sessionIdStr);
//        } catch (NumberFormatException e) {
//            throw new BadCredentialsCustomException("Invalid session format");
//        }
        long sessionId = SupportSessionUtils.parseSessionId(sessionIdStr);

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundCustomException("Session not found"));

        if (!session.getUser()
                    .getLogin()
                    .equals(currentUserLogin)) {
            throw new AccessDeniedCustomException("You cannot invalidate someone else's session");
        }

        sessionRepository.delete(session);
    }

    public void invalidateAllUserSessions(Long userId) {
        if (userId == null) {
            throw new BadCredentialsCustomException("User ID cannot be null");
        }
        sessionRepository.deleteByUserId(userId);
    }

    public HttpResponse<SessionResponseDto> buildSuccessResponse(Session session) {
        final long SESSION_MAX_AGE = sessionProperties.getTtlHours() * SecurityConstants.SECONDS_IN_HOUR;

        UiClient user = session.getUser();

        SessionResponseDto body = SessionResponseDto.builder()
                .login(user.getLogin())
                .uuidDepartament(user.getUuidDepartament())
                .build();

        return HttpResponse.ok(body)
                .cookie(createSessionCookie(session.getId(), SESSION_MAX_AGE));
    }

    public Cookie createSessionCookie(Object value, long maxAge) {
        return Cookie.of(SecurityConstants.COOKIES_NAME, String.valueOf(value))
                .path(SecurityConstants.COOKIES_PATH)
                .httpOnly(true)
                .maxAge(maxAge);
    }

}