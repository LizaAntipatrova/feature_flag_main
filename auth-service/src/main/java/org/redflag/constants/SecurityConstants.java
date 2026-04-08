package org.redflag.constants;

public final class SecurityConstants {

    public static final String ROUTE_MATCH_ATTRIBUTE = "micronaut.http.route.match";
    public static final String CLEANUP_INTERVAL = "${micronaut.security.session.cleanup-interval}";

    public static final String COOKIES_NAME = "SESSION";
    public static final String COOKIES_PATH = "/";
    public static final int SECONDS_IN_HOUR = 3600;

    public static final String UI_CLIENT_ID_NAME = "id";
    public final static String UI_DEPARTMENT_NAME = "uuidDepartament";

    public static final Integer EXPIRATION_TOKEN_SECONDS = 86400;
    public static final String TOKEN_TYPE_SECTION = "type";
    public static final String SDK_TOKEN_TYPE_VALUE = "sdk_client";

    public static final String AUTH_SERVICE_TOKEN_TYPE_VALUE = "auth_service";
    public static final String MAIN_SERVICE_TOKEN_TYPE_VALUE = "main_service";


    public static final String SDK_ACCESS_TOKEN_NAME = "access_token";
    public static final String SDK_AUTH_TOKEN_TYPE = "token_type";
    public static final String SDK_AUTH_TOKEN_VALUE = "Bearer ";

}
