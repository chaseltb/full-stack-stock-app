package learn.lavadonut.data;

import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioRepository {

    List<Portfolio> findPortfoliosByUserId(int userId);
    Portfolio createPortfolio(Portfolio newPortfolio);
    List<Stock> findAllStocksInPortfolio(int userId);
    List<Order> findOrdersByPortfolioId(int userId);
    boolean updateAccountType(Portfolio p);

    Portfolio addStockToPortfolio(int userId, Stock stock);
    boolean sellStockFromPortfolio(int userId, int stockId);
    boolean deleteStockFromPortfolio(int userId, int stockId);
//TODO should the service do the calculation
//    BigDecimal getPortfolioValue(int userId, String date);
//    boolean updateCostBasisOnDividend(int userId, BigDecimal dividend);
//    BigDecimal calculateCapitalGainsTax(List<Order> orders);
}
