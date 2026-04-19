package org.redflag.constants;

import java.util.Set;

public final class LoggingConstants {

    public static final String SENSITIVE_DATA_MASK = "******";

    public static final String GUEST_NO_ID = "guest";

    public static final Set<String> MASKED_FIELDS = Set.of(
            "password", "token", "id_session", "login", "secret", "identity", "username"
    );

    public static final String LOG_PATTERN =
            "Custom LOG: [User: {}] | Method: [{}], Path: [{}], Status: [{}], Time: [{}ms], Request: {}";

    public static final String COMPLEX_BODY_FALLBACK = "[complex body]";
    public static final String EMPTY_BODY = "{}";

}
