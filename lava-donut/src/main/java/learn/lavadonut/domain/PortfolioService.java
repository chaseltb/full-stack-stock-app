package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepo;
    private final StockRepository stockRepo;

    public PortfolioService(PortfolioRepository portfolioRepo, StockRepository stockRepo) {
        this.portfolioRepo = portfolioRepo;
        this.stockRepo = stockRepo;
    }

    public List<Portfolio> findPortfoliosByUserId(int userId) {
        return portfolioRepo.findPortfoliosByUserId(userId);
    }

    public List<Stock> findAllOwnedStocksInPortfolio(int portfolioId) {
        return portfolioRepo.findAllStocksInPortfolio(portfolioId);
    }

    public Result<Portfolio> createPortfolio(Portfolio portfolio) {
        Result<Portfolio> result = new Result<>();

        if (portfolio == null) {
            result.addMessage("Portfolio cannot be null.", ResultType.INVALID);
            return result;
        }

        if (portfolio.getId() >= 1) {
            result.addMessage("Portfolio cannot have an id", ResultType.INVALID);
        }
        if (portfolio.getUserId() <= 0) {
            result.addMessage("Portfolio must be associated with a valid user", ResultType.INVALID);
        }
        if (portfolio.getAccountType() == null) {
            result.addMessage("Portfolio must have an account type", ResultType.INVALID);
        }
        if (!result.isSuccess()) {
            return result;
        }

        //prevent dupe
        List<Portfolio> existing = portfolioRepo.findPortfoliosByUserId(portfolio.getUserId());
        if (existing != null) {
            for (Portfolio p : existing) {
                if (p.getAccountType().equals(portfolio.getAccountType())) {
                    result.addMessage("User already has a portfolio of that account type", ResultType.INVALID);
                    return result;
                }
            }
        }

        Portfolio newPortfolio = portfolioRepo.createPortfolio(portfolio);
        if (newPortfolio == null) {
            result.addMessage("Failed to create portfolio", ResultType.INVALID);
            return result;
        }

        result.setPayload(newPortfolio);
        return result;
    }


    public Result<Portfolio> updateAccountType(Portfolio portfolio) {
        Result<Portfolio> result = new Result<>();
        if (portfolio == null) {
            result.addMessage(
                    String.format("Portfolio must be valid"),
                    ResultType.INVALID);
            return result;
        }
        if (portfolio.getAccountType() == null) {
            result.addMessage(
                    String.format("Account type %s is not a valid account", portfolio.getAccountType()),
                    ResultType.INVALID);
        }
        if (portfolio.getId() == 0) {
            result.addMessage(
                    String.format("Portfolio id %s is not a valid id", portfolio.getId()),
                    ResultType.INVALID);
        }
        List<Portfolio> portfolios = portfolioRepo.findPortfoliosByUserId(portfolio.getUserId());
        if (portfolios == null) {
            result.addMessage("User not found", ResultType.NOT_FOUND);
            return result;
        }
        if (portfolios.stream().noneMatch(p -> p.getId() == portfolio.getId())) {
            result.addMessage(
                    String.format("No account for this user with portfolio id %s", portfolio.getId()),
                    ResultType.INVALID);
        }
        if (!result.isSuccess()){
            return result;
        }
        boolean updated = portfolioRepo.updateAccountType(portfolio);
        if(updated) {
            portfolio.setAccountType(portfolio.getAccountType());
            result.setPayload(portfolio);
        } else {
            result.addMessage("failed to update account type", ResultType.INVALID);
        }
        return result;
    }

    public Result<BigDecimal> getPortfolioValue(int portfolioId, String date) {

        Result<BigDecimal> result = new Result<>();
        Date searchDate;
        try {
            searchDate = Date.valueOf(date);

        } catch (IllegalArgumentException ex) {
            result.addMessage("Date is not correct format (yyyy-mm-dd)", ResultType.INVALID);
            return result;
        }
        List<Order> orders = portfolioRepo.findOrdersByPortfolioId(portfolioId);
        if (orders == null || orders.isEmpty()) {
            result.addMessage(String.format("No Orders found for user %s", portfolioId), ResultType.NOT_FOUND);
            return result;
        }
        List<Stock> allStocks = stockRepo.findAll();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Order order : orders) {
            //want orders before or equal to searchdate
            if(!order.getDate().after(searchDate)) {
                Stock stock = allStocks.stream()
                        .filter(s -> s.getId() == order.getStockId()).findFirst().orElse(null);
                if (stock == null || stock.getCurrentPrice() == null) {
                    continue;
                }
                BigDecimal value = stock.getCurrentPrice().multiply(order.getNumberOfShares());
                if (order.getTransactionType() == TransactionType.BUY) {
                    totalValue = totalValue.add(value);
                } else if (order.getTransactionType() == TransactionType.SELL) {
                    totalValue = totalValue.subtract(value);
                }
            }
        }
        result.setPayload(totalValue);
        return result;
    }

    public Result<Portfolio> updateCostBasisOnDividend(int userId, BigDecimal dividend) {
        Result<Portfolio> result = new Result<>();
        List<Portfolio> portfolios = portfolioRepo.findPortfoliosByUserId(userId);
        if (portfolios == null) {
            result.addMessage("No portfolios for user found", ResultType.NOT_FOUND);
        }
        //TODO Decide how we want to calculate cost basis, either through all orders or all stocks

        return result;
    }

    public Result<BigDecimal> calculateCapitalGainsTax(List<Order> orders, BigDecimal taxRate) {

        Result<BigDecimal> result = new Result<>();
        if (orders == null || orders.isEmpty()) {
            result.setPayload(BigDecimal.ZERO);
            return result;
        }

        //sort by date for orders
        orders.sort(Comparator.comparing(Order::getDate));
        HashMap<Integer, List<Order>> mapStockOrders = new HashMap<>();
        BigDecimal totalGains = BigDecimal.ZERO;

        for (Order order : orders) {
            int stockId = order.getStockId();

            if (order.getTransactionType() == TransactionType.BUY) {
                //add stock to map if buy
                if (mapStockOrders.containsKey(order.getStockId())) {
                    List<Order> os = mapStockOrders.get(order.getStockId());
                    os.add(order);
                    mapStockOrders.put(order.getStockId(), os);

                } else {
                    List<Order> os = new ArrayList<>();
                    os.add(order);
                    mapStockOrders.put(order.getStockId(), os);
                }

            } else if (order.getTransactionType() == TransactionType.SELL) {
                BigDecimal sellShares = order.getNumberOfShares();
                List<Order> buyOrders = mapStockOrders.get(stockId);

                if (buyOrders == null || buyOrders.isEmpty()) {
                    continue;
                    // TODO currently skipping, but maybe throw error ?
                    //result.addMessage(String.format("No buy orders found for stock with id %s", stockId), ResultType.INVALID);
                }


                int i = 0;
                while (sellShares.compareTo(BigDecimal.ZERO) > 0 && i < buyOrders.size()) {
                    Order buyOrder = buyOrders.get(i);
                    BigDecimal boughtShares = buyOrder.getNumberOfShares();

                    BigDecimal sharesSold;
                    //if more sell then buy in this order set sharessold to amount of shares in buy
                    if (sellShares.compareTo(boughtShares) > 0) {
                        sharesSold = boughtShares;
                    } else {
                        sharesSold = sellShares;
                    }


                    BigDecimal gain = order.getPrice().subtract(buyOrder.getPrice()).multiply(sharesSold);
                    totalGains = totalGains.add(gain);


                    buyOrder.setNumberOfShares(boughtShares.subtract(sharesSold));
                    sellShares = sellShares.subtract(sharesSold);

                    if (buyOrder.getNumberOfShares().compareTo(BigDecimal.ZERO) == 0) {
                        buyOrders.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        }
        BigDecimal amountTaxed = totalGains.multiply(taxRate);
        result.setPayload(amountTaxed);
        return result;
    }

    //TODO decide if these should be here or not, watch stock would require DB change
//    public Portfolio addWatchStockToPortfolio(int userId, Stock stock) {
//
//    }
//
//    public boolean deleteWatchStockFromPortfolio(int userId, int stockId){
//
//    }
//    public boolean sellStockFromPortfolio(int userId, int stockId) {
//
//    }
}