package med.voll.api.domain.user;

public enum UserProfileEnum {
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    public final String value;

    private UserProfileEnum(String value) {
        this.value = value;
    }
}
