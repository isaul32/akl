package fi.tite.akl.domain.enumeration;

public enum MembershipRole {
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_CAPTAIN("ROLE_CAPTAIN");

    private final String text;

    MembershipRole(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
