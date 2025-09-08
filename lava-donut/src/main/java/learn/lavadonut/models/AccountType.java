package learn.lavadonut.models;

public enum AccountType {
    RETIREMENT(1, "Retirement"),
    INVESTING(2, "Investing");

    private int id;
    private String name;

    AccountType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
