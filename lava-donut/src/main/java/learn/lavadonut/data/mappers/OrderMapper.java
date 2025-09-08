package learn.lavadonut.data.mappers;

import learn.lavadonut.models.Order;
import learn.lavadonut.models.TransactionType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet resultSet, int i) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("order_id"));
        order.setStockId(resultSet.getInt("stock_id"));
        // order.setUserId(resultSet.getInt("user_id");
        setDateTimeFromResultSet(order, resultSet);
        order.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
        order.setNumberOfShares(resultSet.getDouble("shares"));
        return order;
    }

    private static void setDateTimeFromResultSet(Order order, ResultSet resultSet) throws SQLException {
        // get the timestamp
        Timestamp timestamp = resultSet.getTimestamp("date");

        // get the timezone offset
        String timeZoneOffset = resultSet.getString("timezone_offset");

        if (timestamp != null && timeZoneOffset != null) {
            // Get the retrieved timestamp and offset
            OffsetDateTime offsetDateTime = timestamp.toInstant().atOffset(ZoneOffset.of(timeZoneOffset));
            // Convert the timestamp to Instant and then to ZonedDateTime
            order.setDateTime(offsetDateTime.toZonedDateTime());
        } else {
            order.setDateTime(OffsetDateTime.now().toZonedDateTime());
        }
    }
}
