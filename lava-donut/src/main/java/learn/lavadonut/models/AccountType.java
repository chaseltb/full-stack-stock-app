package learn.lavadonut.models;

public enum AccountType {
    RETIREMENT(1, "Retirement"),
    INVESTMENT(2, "Investment"),
    ROTH_IRA(3,"Roth IRA");

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

    public static AccountType fromValue(String value) {
        for (AccountType type : AccountType.values()) {
            if (type.name.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return RETIREMENT;
    }
}
