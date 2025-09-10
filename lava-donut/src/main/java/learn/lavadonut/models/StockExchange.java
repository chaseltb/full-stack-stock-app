package learn.lavadonut.models;

import java.util.Objects;
import java.util.TimeZone;

public class StockExchange {
    private int id;
    public String name;
    public String code;
    public String timeZone;

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

    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StockExchange)) return false;
        StockExchange that = (StockExchange) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(code, that.code) && Objects.equals(timeZone, that.timeZone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, timeZone);
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
