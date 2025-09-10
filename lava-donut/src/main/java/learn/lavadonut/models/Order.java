package learn.lavadonut.models;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Order {
    // fields
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    private int stockId;
    private BigDecimal numberOfShares; // since it can be a decimal
    private Date date;
    private BigDecimal price;
//    private int userId;

    // constructors
    public Order(){
    }

    public Order(int id, TransactionType transactionType, int stockId, BigDecimal numberOfShares, Date date, BigDecimal price) {
        this.id = id;
        this.transactionType = transactionType;
        this.stockId = stockId;
        this.numberOfShares = numberOfShares;
        this.date = date;
        this.price = price;
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

    public BigDecimal getNumberOfShares() {
        return numberOfShares;
    }

    public void setNumberOfShares(BigDecimal numberOfShares) {
        this.numberOfShares = numberOfShares;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

    // overrides
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", transactionType=" + transactionType +
                ", stockId=" + stockId +
                ", numberOfShares=" + numberOfShares +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && stockId == order.stockId && transactionType == order.transactionType && Objects.equals(numberOfShares.stripTrailingZeros(), order.numberOfShares.stripTrailingZeros()) && Objects.equals(date, order.date) && Objects.equals(price.stripTrailingZeros(), order.price.stripTrailingZeros());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, stockId, numberOfShares, date, price);
    }
}
