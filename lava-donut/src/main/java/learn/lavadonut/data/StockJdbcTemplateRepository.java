package learn.lavadonut.data;

import learn.lavadonut.data.mappers.StockMapper;
import learn.lavadonut.models.Stock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class StockJdbcTemplateRepository implements StockRepository{
    private final JdbcTemplate jdbcTemplate;

    public StockJdbcTemplateRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    //TODO: IF ERROR, RE-EXAMINE STOCK MAPPER
    @Override
    public List<Stock> findAll() {
        final String sql = "select s.stock_id, s.`name`, s.`ticker`, s.asset_type, s.industry, "
                + "se.stock_exchange_id, se.`name`, se.`code`, se.timezone, "
                + "c.country_id, c.`name`, c.`code`, "
                + "cu.currency_id, cu.`name`, cu.`code`, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public List<Stock> getStocksByIndustry(String industry) {
        final String sql = "select s.stock_id, s.`name`, s.`ticker`, s.asset_type, s.industry, "
                + "se.stock_exchange_id, se.`name`, se.`code`, se.timezone, "
                + "c.country_id, c.`name`, c.`code`, "
                + "cu.currency_id, cu.`name`, cu.`code`, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + " where s.industry = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public Stock findById(int stockId) {
        final String sql = "select s.stock_id, s.`name`, s.`ticker`, s.asset_type, s.industry, "
                + "se.stock_exchange_id, se.`name`, se.`code`, se.timezone, "
                + "c.country_id, c.`name`, c.`code`, "
                + "cu.currency_id, cu.`name`, cu.`code`, cu.value_to_usd "
                + "from stock_exchange se "
                + "inner join stocks s on se.stock_exchange_id = s.stock_exchange_id "
                + "inner join countries c on s.country_id = c.country_id "
                + "inner join currencies cu on c.currency_id = cu.currency_id "
                + " where s.stock_id = ?;";

        return jdbcTemplate.query(sql, new StockMapper(), stockId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public Stock findByTicker(String ticker) {
        final String sql = "select s.stock_id, s.`name`, s.`ticker`, s.asset_type, s.industry, "
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
        return null;
    }

    @Override
    public boolean update(Stock stock) {
        return false;
    }

    @Override
    public boolean deleteById(int stockId) {
        return false;
    }
}
