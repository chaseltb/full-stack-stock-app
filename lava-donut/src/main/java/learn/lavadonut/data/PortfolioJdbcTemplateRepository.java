package learn.lavadonut.data;

import learn.lavadonut.data.mappers.OrderMapper;
import learn.lavadonut.data.mappers.PortfolioMapper;
import learn.lavadonut.data.mappers.StockMapper;
import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class PortfolioJdbcTemplateRepository implements PortfolioRepository {


    private final JdbcTemplate jdbcTemplate;

    public PortfolioJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Portfolio createPortfolio(Portfolio newPortfolio) {
        final String sql = "insert into portfolio (user_id, account_type) " +
                " values (?, ?); ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newPortfolio.getUserId());
            ps.setString(2, newPortfolio.getAccountType().getName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        newPortfolio.setId(keyHolder.getKey().intValue());
        return newPortfolio;
    }

    @Override
    public List<Portfolio> findPortfoliosByUserId(int userId) {
        final String sql = "select portfolio_id, user_id, account_type from portfolio where user_id = ?;";
        List<Portfolio> portfolios = jdbcTemplate.query(sql, new PortfolioMapper(), userId);

        if (portfolios != null) {
            for (Portfolio p : portfolios) {
                p.setStocks(findAllStocksInPortfolio(p.getId()));
                p.setOrders(findOrdersByPortfolioId(p.getId()));
            }
        }
        return portfolios;
    }

    @Override
    public Portfolio findPortfolioById(int portfolioId) {
        final String sql = "select portfolio_id, user_id, account_type from portfolio where portfolio_id = ?;";
        Portfolio portfolio = jdbcTemplate.queryForObject(sql, new PortfolioMapper(), portfolioId);

        if (portfolio != null) {
            portfolio.setStocks(findAllStocksInPortfolio(portfolio.getId()));
            portfolio.setOrders(findOrdersByPortfolioId(portfolio.getId()));

        }
        return portfolio;
    }

    @Override
    public List<Stock> findAllStocksInPortfolio(int portfolioId) {

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
                        "where p.portfolio_id = ?;";


        return jdbcTemplate.query(sql, new StockMapper(), portfolioId);
    }

    @Override
    public List<Order> findOrdersByPortfolioId(int portfolioId) {
        final String sql = "select o.order_id, o.transaction_type, o.shares, " +
        " o.price, o.date, o.stock_id, p.portfolio_id, p.user_id " +
                "from orders o inner join portfolio_orders po on o.order_id = po.order_id " +
                "inner join portfolio p on po.portfolio_id = p.portfolio_id " +
                "where p.portfolio_id = ?;";

        return jdbcTemplate.query(sql, new OrderMapper(), portfolioId);
    }

    @Override
    public boolean updateAccountType(Portfolio p) {
        final String sql = "update portfolio set " +
        "account_type = ? where portfolio_id = ?;";
        return jdbcTemplate.update(sql, p.getAccountType().getName(), p.getId()) > 0;
    }


    @Override
    public boolean addOrderToPortfolio(int portfolioId, int orderId) {
        final String sql = "insert into portfolio_orders (portfolio_id, order_id) values (?, ?);";
        return jdbcTemplate.update(sql, portfolioId, orderId) > 0;
    }

    @Override
    public boolean removeOrderFromPortfolio(int portfolioId, int orderId) {
        final String sql = "delete from portfolio_orders where portfolio_id = ? and order_id = ?;";
        return jdbcTemplate.update(sql, portfolioId, orderId) > 0;
    }

}
