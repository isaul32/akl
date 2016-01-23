package com.pyrenty.akl.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String CMS = "ROLE_CMS";

    public static final String REFEREE = "ROLE_REFEREE";

    public static final String CAPTAIN = "ROLE_CAPTAIN";

    public static final String PLAYER = "ROLE_PLAYER";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
