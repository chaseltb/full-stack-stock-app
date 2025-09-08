package learn.lavadonut.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    // fields
    private int id;
    private TransactionType transactionType;
    private Stock stock;
    private double numberOfShares; // since it can be a decimal
    private LocalDateTime dateTime;
    private BigDecimal price;
    private User user;

    // constructors
    public Order(){
    }

    public Order(int id, TransactionType transactionType, Stock stock, double numberOfShares, LocalDateTime dateTime, BigDecimal price, User user) {
        this.id = id;
        this.transactionType = transactionType;
        this.stock = stock;
        this.numberOfShares = numberOfShares;
        this.dateTime = dateTime;
        this.price = price;
        this.user = user;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public double getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(double numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // overrides
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", transactionType=" + transactionType +
                ", stock=" + stock +
                ", numberOfShares=" + numberOfShares +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Double.compare(numberOfShares, order.numberOfShares) == 0 && transactionType == order.transactionType && Objects.equals(stock, order.stock) && Objects.equals(dateTime, order.dateTime) && Objects.equals(price, order.price) && Objects.equals(user, order.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, stock, numberOfShares, dateTime, price, user);
    }
}
