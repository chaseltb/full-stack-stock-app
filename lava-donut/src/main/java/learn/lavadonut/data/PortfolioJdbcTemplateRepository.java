package learn.lavadonut.data;

import learn.lavadonut.data.mappers.OrderMapper;
import learn.lavadonut.data.mappers.StockMapper;
import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class PortfolioJdbcTemplateRepository implements PortfolioRepository {


    private final JdbcTemplate jdbcTemplate;

    public PortfolioJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Portfolio findByUserId(int userId) {
        final String sql = "Select * from portfolio where user_id = ?;";
        return null;
    }

    @Override
    public List<Stock> findAllStocksInPortfolio(int userId) {

        final String sql =
                "Select s.stock_id, s.ticker, s.name, s.asset_type, s.industry, s.country_id" +
                        " from portfolio p inner join portfolio_orders po " +
                        "on p.portfolio_id = po.portfolio_id inner join orders o on " +
                        "o.order_id = po.order_id inner join stocks s " +
                        "on o.stock_id = s.stock_id" +
                        "where p.user_id = ?; ";

        return jdbcTemplate.query(sql, new StockMapper(), userId);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        final String sql = "select o.order_id, o.transaction_type, o.shares, o.price, o.date, o.stock_id " +
                "from orders o inner join portfolio_orders po on o.order_id = po.order_id " +
                "inner join portfolio p on po.portfolio_id = p.portfolio_id " +
                "where p.user_id = ?;";

        return jdbcTemplate.query(sql, new OrderMapper(), userId);
    }

    @Override
    public boolean updateAccountType(int userId, AccountType accountType) {
        final String sql = "update portfolio set " +
        "account_type = ?;";
        return jdbcTemplate.update(sql, accountType.getName()) > 0;
    }

//TODO decide if these are needed
    @Override
    public boolean sellStockFromPortfolio(int userId, int stockId) {
        return false;
    }
    @Override
    public Portfolio addStockToPortfolio(int userId, Stock stock) {
        return null;
    }

    @Override
    public boolean deleteStockFromPortfolio(int userId, int stockId) {
        return false;
    }

}
