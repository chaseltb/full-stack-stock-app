package learn.lavadonut.data;

import learn.lavadonut.data.mappers.OrderMapper;
import learn.lavadonut.models.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderJdbcTemplateRepository implements OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order add(Order order) {
        return null;
    }

    @Override
    public Order findById(int id) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        final String sql = "select order_id, stock_id, transaction_type, shares, price, date from order;";
        return jdbcTemplate.query(sql, new OrderMapper());
    }

    @Override
    public List<Order> findByUser(int userId) {
        return List.of();
    }

    @Override
    public List<Order> findByStock(int stockId) {
        return List.of();
    }

    @Override
    public boolean update(Order order) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
