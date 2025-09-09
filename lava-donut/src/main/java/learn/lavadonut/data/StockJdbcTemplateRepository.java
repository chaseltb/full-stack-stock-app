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
        final String sql = "select stock_id, `name`, `ticker`, asset_type, industry, stock_exchange_id, country_id "
                + "from stocks limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public List<Stock> getStocksByIndustry(String industry) {
        final String sql = "select stock_id, `name`, `ticker`, asset_type, industry, stock_exchange_id, country_id "
                + "from stocks "
                + "where industry = ? limit 1000;";
        return jdbcTemplate.query(sql, new StockMapper());
    }

    @Override
    public Stock findById(int stockId) {
        final String sql = "select stock_id, `name`, `ticker`, asset_type, industry, stock_exchange_id, country_id "
                + "from stocks "
                + "where stock_id = ?;";

        return jdbcTemplate.query(sql, new StockMapper(), stockId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public Stock findByTicker(String ticker) {
        final String sql = "select stock_id, `name`, `ticker`, asset_type, industry, stock_exchange_id, country_id "
                + "from stocks "
                + "where `ticker` = ?;";

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
