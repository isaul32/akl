package com.pyrenty.akl.domain.enumeration;

public enum MembershipRoles {
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_STANDIN("ROLE_STANDIN"),
    ROLE_CAPTAIN("ROLE_CAPTAIN");

    private final String text;

    MembershipRoles(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
