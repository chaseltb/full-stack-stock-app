package learn.lavadonut.data;

import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;

import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.util.List;

public interface PortfolioRepository {

    List<Portfolio> findPortfoliosByUserId(int userId);
    Portfolio findPortfolioById(int portfolioId);
    Portfolio createPortfolio(Portfolio newPortfolio);
    List<Stock> findAllStocksInPortfolio(int userId);
    List<Order> findOrdersByPortfolioId(int userId);
    boolean updateAccountType(Portfolio p);
    boolean addOrderToPortfolio(int portfolioId, int orderId);
    boolean removeOrderFromPortfolio(int portfolioId, int orderId);

}
