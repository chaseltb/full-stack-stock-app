package learn.lavadonut.models;

import java.util.List;
import java.util.Objects;

public class Portfolio {

    private int id;
    private int userId;
    private List<Stock> stocks;
    private List<Order> orders;
    private List<Stock> watchedStocks;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    private AccountType accountType;

    public List<Stock> getWatchedStocks() {
        return watchedStocks;
    }

    public void setWatchedStocks(List<Stock> watchedStocks) {
        this.watchedStocks = watchedStocks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Portfolio)) return false;
        Portfolio portfolio = (Portfolio) o;
        return id == portfolio.id && userId == portfolio.userId && Objects.equals(stocks, portfolio.stocks) && accountType == portfolio.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, stocks, accountType);
    }
}
