package learn.lavadonut.models;

import java.math.BigDecimal;

public class Currency {

    private int id;
    private String name;
    private String code;
    private BigDecimal valueToUsd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getValueToUsd() {
        return valueToUsd;
    }

    public void setValueToUsd(BigDecimal valueToUsd) {
        this.valueToUsd = valueToUsd;
    }
}
