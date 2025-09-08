package learn.lavadonut.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Order {
    // fields
    private int id;
    private TransactionType transactionType;
    private int stockId;
    private double numberOfShares; // since it can be a decimal
    private ZonedDateTime dateTime;
    private BigDecimal price;
    private int userId;

    // constructors
    public Order(){
    }

    public Order(int id, TransactionType transactionType, int stockId, double numberOfShares, ZonedDateTime dateTime, BigDecimal price, int userId) {
        this.id = id;
        this.transactionType = transactionType;
        this.stockId = stockId;
        this.numberOfShares = numberOfShares;
        this.dateTime = dateTime;
        this.price = price;
        this.userId = userId;
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

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public double getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(double numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // overrides
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", transactionType=" + transactionType +
                ", stockId=" + stockId +
                ", numberOfShares=" + numberOfShares +
                ", dateTime=" + dateTime +
                ", price=" + price +
                ", userId=" + userId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && stockId == order.stockId && Double.compare(numberOfShares, order.numberOfShares) == 0 && userId == order.userId && transactionType == order.transactionType && Objects.equals(dateTime, order.dateTime) && Objects.equals(price, order.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, stockId, numberOfShares, dateTime, price, userId);
    }
}
