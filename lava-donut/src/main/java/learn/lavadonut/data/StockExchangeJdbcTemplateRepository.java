package learn.lavadonut.data;

import learn.lavadonut.data.mappers.StockExchangeMapper;
import learn.lavadonut.models.StockExchange;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StockExchangeJdbcTemplateRepository implements StockExchangeRepository {
    private final JdbcTemplate jdbcTemplate;

    public StockExchangeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<StockExchange> findAll() {
        final String sql = "select stock_exchange_id, `name`, `code` " +
                "from stock_exchange;";
        return jdbcTemplate.query(sql, new StockExchangeMapper());
    }

    @Override
    public StockExchange findById(int id) {
        final String sql = "select stock_exchange_id, `name`, `code`, time_zone " +
                "from stock_exchange where stock_exchange_id = ?;";
        return jdbcTemplate.query(sql, new StockExchangeMapper(), id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public StockExchange add(StockExchange exchange) {
        final String sql = "insert into stock_exchange (`name`, `code`, time_zone) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, exchange.getName());
            ps.setString(2, exchange.getCode());
            ps.setString(3, exchange.getTimeZone());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        exchange.setId(keyHolder.getKey().intValue());
        return exchange;
    }

    @Override
    public boolean update(StockExchange exchange) {
        final String sql = "update stock_exchange set " +
                "`name` = ?, `code` = ?, time_zone = ? " +
                "where stock_exchange_id = ?;";

        return jdbcTemplate.update(sql, exchange.getName(), exchange.getCode(), exchange.getTimeZone(),
               exchange.getId()) > 0;
    }

    @Override
    public boolean deleteById(int stockExchangeId) {
        final String sql = "delete from stock_exchange where stock_exchange_id = ?;";
        return jdbcTemplate.update(sql, stockExchangeId) > 0;
    }
}

