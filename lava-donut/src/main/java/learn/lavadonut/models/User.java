package learn.lavadonut.models;

import java.util.Objects;

public class User {
    private int userId;
    private int currencyId;
    private String username;
    private String passwordHashed;
    private String firstName;
    private String lastName;
    private UserType permission;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserType getPermission() {
        return permission;
    }

    public void setPermission(UserType permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId && currencyId == user.currencyId && Objects.equals(username, user.username) && Objects.equals(passwordHashed, user.passwordHashed) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && permission == user.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currencyId, username, passwordHashed, firstName, lastName, permission);
    }
}
