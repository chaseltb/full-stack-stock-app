package learn.lavadonut.models;

public class Country {

    private int id;
    private Currency currency;
    private String name;
    private String code;

    public Country() {
    }

    public Country(int id, Currency currency, String name, String code) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
