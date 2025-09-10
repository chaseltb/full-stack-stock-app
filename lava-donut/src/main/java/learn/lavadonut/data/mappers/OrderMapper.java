package learn.lavadonut.data.mappers;

import learn.lavadonut.models.Order;
import learn.lavadonut.models.TransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("order_id"));
        order.setStockId(resultSet.getInt("stock_id"));
        // order.setUserId(resultSet.getInt("user_id");
        order.setDate(resultSet.getDate("date"));
//        // Handling TransactionType enum
//        String transactionTypeStr = resultSet.getString("transaction_type");
//        if (transactionTypeStr != null) {
//            try {
//                order.setTransactionType(TransactionType.valueOf(transactionTypeStr));
//            } catch (IllegalArgumentException e) {
//                order.setTransactionType(TransactionType.BUY); // default
//            }
//        }
        order.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
        order.setNumberOfShares(BigDecimal.valueOf(resultSet.getDouble("shares")));
        order.setPrice(BigDecimal.valueOf(resultSet.getDouble("price")));
        return order;
    }
}
