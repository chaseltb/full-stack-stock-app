package learn.lavadonut.models;

public enum UserType {
    ADMIN("admin"),
    USER("user");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
