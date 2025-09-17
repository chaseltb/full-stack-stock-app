package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sound.sampled.Port;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final OrderService orderService;
    private final PortfolioRepository portfolioRepo;
    private final StockRepository stockRepo;
    private final OrderRepository orderRepo;

    public PortfolioService(OrderService orderService, PortfolioRepository portfolioRepo, StockRepository stockRepo, OrderRepository orderRepo) {
        this.orderService = orderService;
        this.portfolioRepo = portfolioRepo;
        this.stockRepo = stockRepo;
        this.orderRepo = orderRepo;
    }

    public Portfolio findPortfolioById(int portfolioId) {
        return portfolioRepo.findPortfolioById(portfolioId);
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

    public Result<Portfolio> addOrderToPortfolio(int portfolioId, Order newOrder) {
        Result<Portfolio> result = new Result<>();
        Result<Order> orderResult = orderService.add(newOrder);
        if (!orderResult.isSuccess()) {
            result.addMessage("Could not create order: " + orderResult.getMessages(), ResultType.INVALID);
            return result;
        }
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);
        if (portfolio == null) {
            result.addMessage(
                    String.format("Portfolio must be valid"),
                    ResultType.NOT_FOUND);
            return result;
        }

        boolean success = portfolioRepo.addOrderToPortfolio(portfolioId, orderResult.getPayload().getId());
        if (!success) {
            result.addMessage("Failed to link order to portfolio", ResultType.INVALID);
            return result;
        }
        Portfolio p = portfolioRepo.findPortfolioById(portfolioId);
        result.setPayload(p);
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
            result.addMessage(String.format("No Orders found for portfolio %s", portfolioId), ResultType.NOT_FOUND);
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

    public Result<Map<Stock, BigDecimal>> getStockToTotalShares(int portfolioId) {

        Result<Map<Stock,BigDecimal>> result = new Result<>();
        List<Order> orders = portfolioRepo.findOrdersByPortfolioId(portfolioId);
        if (orders == null || orders.isEmpty()) {
            result.addMessage(String.format("No Orders found for portfolio %s", portfolioId), ResultType.NOT_FOUND);
            return result;
        }

        List<Stock> stocks = portfolioRepo.findAllStocksInPortfolio(portfolioId);
        if (stocks == null || stocks.isEmpty()) {
            result.addMessage(String.format("No Stocks found for portfolio %s", portfolioId), ResultType.NOT_FOUND);
            return result;
        }
        Map<Stock, BigDecimal> stockToShares = stocks.stream()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> orders.stream()
                                .filter(o -> o.getStockId() == s.getId() && o.getTransactionType() != TransactionType.DIVIDEND)
                                .map(o -> o.getTransactionType() == TransactionType.BUY ? o.getNumberOfShares() : o.getNumberOfShares().multiply(BigDecimal.valueOf(-1)))
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ));
        result.setPayload(stockToShares);

        return result;
    }

    public Result<Portfolio> updateCostBasisOnDividend(int portfolioId, int stockId, BigDecimal dividend) {
        Result<Portfolio> result = new Result<>();
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);
        if (portfolio == null) {
            result.addMessage("No portfolio was found", ResultType.NOT_FOUND);
        }

        List<Order> orders = portfolio.getOrders();
        if (orders == null) {
            result.addMessage("No orders found in portfolio", ResultType.NOT_FOUND);
        }
        BigDecimal stockShares = orders.stream().filter(o -> o.getTransactionType() == TransactionType.BUY)
                .map(Order::getNumberOfShares)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (stockShares.compareTo(BigDecimal.ZERO) == 0) {
            result.addMessage("No shares of this stock in portfolio", ResultType.NOT_FOUND);
            return result;
        }
        BigDecimal totalDividend = stockShares.multiply(dividend);

        Order dividendOrder = new Order();
        dividendOrder.setPrice(totalDividend);
        dividendOrder.setStockId(stockId);
        dividendOrder.setDate(Date.valueOf(LocalDate.now()));
        dividendOrder.setTransactionType(TransactionType.DIVIDEND);
        dividendOrder.setNumberOfShares(stockShares);
        orderRepo.add(dividendOrder);
        orders.add(dividendOrder);
        portfolio.setOrders(orders);
        result.setPayload(portfolio);

        return result;
    }

    public Result<Map<Stock, BigDecimal>> getCostBasisAllStocks(int portfolioId) {

        Result<Map<Stock, BigDecimal>> result = new Result<>();
        Portfolio portfolio = portfolioRepo.findPortfolioById(portfolioId);
        if (portfolio == null) {
            result.addMessage("No portfolio was found", ResultType.NOT_FOUND);
            return result;
        }

        if (portfolio.getStocks().isEmpty() || portfolio.getOrders().isEmpty()) {
            result.addMessage("No orders or stocks found", ResultType.NOT_FOUND);
            return result;
        }
        Map<Stock, BigDecimal> stockToCostBasis = new HashMap<>();

        for (Stock stock : portfolio.getStocks()) {
            List<Order> stockOrders = new ArrayList<>();
            for (Order o : portfolio.getOrders()) {
                if (o.getStockId() == stock.getId()) {
                    stockOrders.add(o);
                }
            }

            Result<BigDecimal> stockCostBasis = calculateCostBasis(stockOrders);
            if (stockCostBasis.isSuccess()) {
                stockToCostBasis.put(stock, stockCostBasis.getPayload());
            }
        }

        result.setPayload(stockToCostBasis);


        return result;
    }

    public Result<BigDecimal> getCostBasisForStock(int portfolioId, int stockId) {
        List<Order> orders = portfolioRepo.findOrdersByPortfolioId(portfolioId);

        Result<BigDecimal> result = new Result<>();
        if (orders == null || orders.isEmpty()) {
            result.addMessage("No orders found", ResultType.NOT_FOUND);
            return result;
        }
        List<Order> stockOrders = new ArrayList<>();
        for (Order o : orders) {
            if (o.getStockId() == stockId) {
                stockOrders.add(o);
            }
        }
        if (stockOrders.isEmpty()) {
            result.addMessage("No orders found for stock Id " + stockId, ResultType.NOT_FOUND);
            return result;
        }

        result = calculateCostBasis(stockOrders);
        return result;
    }

    public Result<BigDecimal> calculateCostBasis(List<Order> orders) {
        Result<BigDecimal> result = new Result<>();
        result.setPayload(BigDecimal.ZERO);
        if (orders == null || orders.isEmpty()) {
            result.addMessage("No orders found", ResultType.NOT_FOUND);
            return result;
        }

        BigDecimal totalValue = BigDecimal.ZERO;
        BigDecimal totalShares = BigDecimal.ZERO;

        for (Order order : orders) {
            BigDecimal shares = order.getNumberOfShares();
            BigDecimal cost = shares.multiply(order.getPrice());

            if (order.getTransactionType() == TransactionType.BUY) {
                totalValue = totalValue.add(cost);
                totalShares = totalShares.add(shares);

            } else if (order.getTransactionType() == TransactionType.SELL) {
                if (totalShares.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal avgCostPerShare = totalValue.divide(totalShares, 2, RoundingMode.HALF_UP);
                    BigDecimal costReduction = avgCostPerShare.multiply(shares);

                    totalShares = totalShares.subtract(shares);
                    totalValue = totalValue.subtract(costReduction);
                }
            } else if (order.getTransactionType() == TransactionType.DIVIDEND) {
                totalValue = totalValue.subtract(order.getPrice());
            }
        }

        if (totalShares.compareTo(BigDecimal.ZERO) > 0) {
             result.setPayload(totalValue.divide(totalShares, 2, RoundingMode.HALF_UP));
        } else {
            result.addMessage("No shares found", ResultType.NOT_FOUND);
        }
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


}