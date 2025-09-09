package learn.lavadonut.data;

import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioRepository {

    Portfolio findByUserId(int userId);
    Portfolio addStockToPortfolio(int userId, Stock stock);
    boolean deleteStockFromPortfolio(int userId, int stockId);
    List<Stock> findAllStocksInPortfolio(int userId);
    List<Order> findOrdersByUserId(int userId);
    boolean updateAccountType(int userId, AccountType accountType);
    boolean sellStockFromPortfolio(int userId, int stockId);
//TODO should the service do the calculation
//    BigDecimal getPortfolioValue(int userId, String date);
//    boolean updateCostBasisOnDividend(int userId, BigDecimal dividend);
//    BigDecimal calculateCapitalGainsTax(List<Order> orders);
}
