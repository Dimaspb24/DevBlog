package com.project.devblog.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OpenApiConstant {
    public static final String API_DESCRIPTION = "Dev Blog application";
    public static final String API_TITLE = "Application API";

    public static final String BEARER_SECURITY_SCHEME = "bearerAuthScheme";
    public static final String BEARER_SCHEME_NAME = "bearer";
    public static final String BEARER_FORMAT = "JWT";

    public static final String DESCRIPTION_DOC = "Dev Blog repository";
    public static final String URL_DOC = "https://github.com/Dimaspb24/DevBlog";

    public static final String URL_LOCAL_SERVER = "http://localhost:8080";
    public static final String DESCRIPTION_LOCAL_SERVER = "Local service";

    public static final String CONTACT_NAME = "API Support";
    public static final String CONTACT_EMAIL = "diman5178@gmail.com";
}
