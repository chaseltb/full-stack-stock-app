package learn.lavadonut.models;

public enum TransactionType {
    BUY(1, "Buy"),
    SELL(2, "Sell"),
    DIVIDEND(3, "Dividend");

    private int id;
    private String name;

    TransactionType(int id, String name) {
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
