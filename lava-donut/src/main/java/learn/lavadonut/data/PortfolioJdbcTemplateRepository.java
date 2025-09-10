package learn.lavadonut.data;

import learn.lavadonut.data.mappers.OrderMapper;
import learn.lavadonut.data.mappers.PortfolioMapper;
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
        final String sql = "select portfolio_id, user_id, account_type from portfolio where user_id = ?;";
        Portfolio portfolio = jdbcTemplate.queryForObject(sql, new PortfolioMapper(), userId);

        if (portfolio != null) {
            portfolio.setStocks(findAllStocksInPortfolio(userId));
            portfolio.setOrders(findOrdersByUserId(userId));
        }
        return portfolio;
    }

    @Override
    public List<Stock> findAllStocksInPortfolio(int userId) {

        final String sql =
                "select s.stock_id, s.name as stock_name, s.current_price, s.ticker, s.asset_type, s.industry, c.country_id, " +
                        "c.name as country_name, c.code as country_code, " +
                        "cur.currency_id, cur.name as currency_name, " +
                        "cur.code as currency_code, cur.value_to_usd, " +
                        "se.stock_exchange_id, se.name as exchange_name, " +
                        "se.code as exchange_code, se.timezone " +
                        "from portfolio p " +
                        "inner join portfolio_orders po on p.portfolio_id = po.portfolio_id " +
                        "inner join orders o on po.order_id = o.order_id " +
                        "inner join stocks s on o.stock_id = s.stock_id " +
                        "inner join countries c on s.country_id = c.country_id " +
                        "inner join currencies cur on c.currency_id = cur.currency_id " +
                        "inner join stock_exchange se on s.stock_exchange_id = se.stock_exchange_id " +
                        "where p.user_id = ?;";


        return jdbcTemplate.query(sql, new StockMapper(), userId);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        final String sql = "select o.order_id, o.transaction_type, o.shares, " +
        " o.price, o.date, o.stock_id, p.user_id " +
                "from orders o inner join portfolio_orders po on o.order_id = po.order_id " +
                "inner join portfolio p on po.portfolio_id = p.portfolio_id " +
                "where p.user_id = ?;";

        return jdbcTemplate.query(sql, new OrderMapper(), userId);
    }

    @Override
    public boolean updateAccountType(int userId, AccountType accountType) {
        final String sql = "update portfolio set " +
        "account_type = ? where user_id = ?;";
        return jdbcTemplate.update(sql, accountType.getName(), userId) > 0;
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
