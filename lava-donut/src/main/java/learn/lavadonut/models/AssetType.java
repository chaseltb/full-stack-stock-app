package learn.lavadonut.models;

public enum AssetType {
    STOCK(1, "Stock"),
    BOND(2,"Bond"),
    ETF(3, "ETF");

    private int id;
    private String name;


    AssetType(int id, String name) {
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
