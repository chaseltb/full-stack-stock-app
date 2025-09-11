package learn.lavadonut.data;

import learn.lavadonut.data.mappers.StockMapper;
import learn.lavadonut.models.Stock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class StockJdbcTemplateRepository implements StockRepository{
    private final JdbcTemplate jdbcTemplate;

    public StockJdbcTemplateRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    //TODO: IF ERROR, RE-EXAMINE STOCK MAPPER
    @Override
    public List<Stock> findAll() {
        final String sql = "select s.stock_id, s.`name` as stock_name, s.`ticker`, s.asset_type, s.industry, s.current_price, "
                + "se.stock_exchange_id, se.`name` as exchange_name, se.`code` as exchange_code, se.timezone, "
                + "c.country_id, c.`name` as country_name, c.`code` as country_code, "
                + "cu.currency_id, cu.`name` as currency_name, cu.`code` as currency_code, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public List<Stock> getStocksByIndustry(String industry) {
        final String sql = "select s.stock_id, s.`name` as stock_name, s.`ticker`, s.asset_type, s.industry, s.current_price, "
                + "se.stock_exchange_id, se.`name` as exchange_name, se.`code` as exchange_code, se.timezone, "
                + "c.country_id, c.`name` as country_name, c.`code` as country_code, "
                + "cu.currency_id, cu.`name` as currency_name, cu.`code` as currency_code, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + "where s.industry = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper(), industry);
    }

    @Override
    public Stock findById(int stockId) {
        final String sql = "select s.stock_id, s.`name` as stock_name, s.`ticker`, s.asset_type, s.industry, s.current_price, "
                + "se.stock_exchange_id, se.`name` as exchange_name, se.`code` as exchange_code, se.timezone, "
                + "c.country_id, c.`name` as country_name, c.`code` as country_code, "
                + "cu.currency_id, cu.`name` as currency_name, cu.`code` as currency_code, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + "where s.stock_id = ?;";

        return jdbcTemplate.query(sql, new StockMapper(), stockId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public Stock findByTicker(String ticker) {
        final String sql = "select s.stock_id, s.`name`, s.`ticker`, s.asset_type, s.industry, s.current_price, "
                + "se.stock_exchange_id, se.`name`, se.`code`, se.timezone, "
                + "c.country_id, c.`name`, c.`code`, "
                + "cu.currency_id, cu.`name`, cu.`code`, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + " where s.`ticker` = ?;";

        return jdbcTemplate.query(sql, new StockMapper(), ticker)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public Stock add(Stock stock) {
        final String sql = "insert into stocks (`name`, `ticker`, asset_type, industry, current_price, stock_exchange_id, country_id) "
                + "values (?, ?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, stock.getName());
            ps.setString(2, stock.getTicker());
            ps.setString(3, stock.getAssetType().name());
            ps.setString(4, stock.getIndustry());
            ps.setBigDecimal(5, stock.getCurrentPrice());
            ps.setInt(6, stock.getStockExchange().getId());
            ps.setInt(7, stock.getCountry().getId());

            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        stock.setId(keyHolder.getKey().intValue());

        return stock;
    }

    @Override
    public boolean update(Stock stock) {
        final String sql = "update stocks set "
                + "`name` = ?, 'ticker' = ?, asset_type = ?, industry = ?, current_price = ?, stock_exchange_id = ?, country_id = ? "
                + "where stock_id = ?;";

        return jdbcTemplate.update(sql,
                stock.getName(),
                stock.getTicker(),
                stock.getAssetType(),
                stock.getIndustry(),
                stock.getCurrentPrice(),
                stock.getStockExchange().getId(),
                stock.getCountry().getId(),
                stock.getId()) > 0;
    }

    @Override
    public boolean deleteById(int stockId) {
        return jdbcTemplate.update(
                "delete from stocks where stock_id = ?;",
                stockId) > 0;
    }

    @Override
    public int getUsageCount(int stockId) {
        return jdbcTemplate.queryForObject(
                "select count(*) from orders where stock_id = ?;",
                Integer.class, stockId);
    }
}
