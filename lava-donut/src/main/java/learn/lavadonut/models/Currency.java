package learn.lavadonut.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        this.valueToUsd = valueToUsd.setScale(4, RoundingMode.HALF_EVEN).stripTrailingZeros();
    }

    //DOES NOT COMPARE IDS
    public boolean equals(Currency other){
        return (this.name.equalsIgnoreCase(other.name)
                && this.code.equalsIgnoreCase(other.code)
                && this.getValueToUsd().compareTo(other.valueToUsd) == 0);
    }
}
