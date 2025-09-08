package learn.lavadonut.data;

import learn.lavadonut.data.mappers.OrderMapper;
import learn.lavadonut.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderJdbcTemplateRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order add(Order order) {
        final String sql = "insert into orders (stock_id, transaction_type, shares, price, `date`) " +
                "values (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getStockId());
            ps.setString(2, order.getTransactionType().name());
            ps.setDouble(3, order.getNumberOfShares());
            ps.setBigDecimal(4, order.getPrice());
            ZonedDateTime zonedDateTime = order.getDateTime();
            if (zonedDateTime != null) {
                ps.setTimestamp(5, Timestamp.from(zonedDateTime.toInstant()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        order.setId(keyHolder.getKey().intValue());
        return order;
    }

    @Override
    public Order findById(int id) {
        String sql = "select order_id, stock_id, transaction_type, shares, price, `date` " +
                "from orders " +
                "where order_id = ?;";

        return jdbcTemplate.query(sql, new OrderMapper(), id).stream()
                .findFirst().orElse(null);
    }

    @Override
    public List<Order> findAll() {
        final String sql = "select order_id, stock_id, transaction_type, shares, price, `date` from orders`;";
        return jdbcTemplate.query(sql, new OrderMapper());
    }

//    @Override
//    public List<Order> findByUser(int userId) {
//        String sql = "select order_id, stock_id, transaction_type, shares, price, `date` " +
//                "from orders " +
//                "where user_id = ?;";
//
//        return jdbcTemplate.query(sql, new OrderMapper(), userId);
//    }

    @Override
    public List<Order> findByStock(int stockId) {
        String sql = "select order_id, stock_id, transaction_type, shares, price, `date` " +
                "from orders " +
                "where stock_id = ?;";

        return jdbcTemplate.query(sql, new OrderMapper(), stockId);
    }

    @Override
    public boolean update(Order order) {
        final String sql = "update orders set " +
                "stock_id = ?, " +
                "transaction_type = ?, " +
                "shares = ?, " +
                "price = ?, " +
                "`date` = ? " +
                "where order_id = ?;";

        return jdbcTemplate.update(sql,
                order.getStockId(), order.getTransactionType(), order.getNumberOfShares(), order.getPrice(), order.getDateTime(), order.getId()) > 0;
    }

    @Override
    public boolean delete(int id) {
        final String sql = "delete from orders where order_id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
