package learn.lavadonut.models;

import java.util.Objects;

public class User {
    private int userId;
    private int currencyId;
    private String firstName;
    private String lastName;
    private int appUserId;

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

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userId == user.userId && currencyId == user.currencyId && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && appUserId == user.appUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currencyId, firstName, lastName, appUserId);
    }
}
