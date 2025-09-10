package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.models.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository repo;

    public PortfolioService(PortfolioRepository repo) {
        this.repo = repo;
    }

    public Portfolio findByUserId(int userId) {
        return repo.findByUserId(userId);
    }

    public List<Stock> findAllOwnedStocksInPortfolio(int userId) {
        return repo.findAllStocksInPortfolio(userId);
    }
    public Result<Portfolio> updateAccountType(int userId, AccountType accountType) {
        Result<Portfolio> result = new Result<>();
        Portfolio portfolio = repo.findByUserId(userId);
        if (portfolio == null) {
            result.addMessage("User not found", ResultType.NOT_FOUND);
        }
        boolean updated = repo.updateAccountType(userId, accountType);
        if(updated) {
            portfolio.setAccountType(accountType);
            result.setPayload(portfolio);
        } else {
            result.addMessage("failed to update account type", ResultType.INVALID);
        }
        return result;
    }
    //TODO should this return a result?
    public BigDecimal getPortfolioValue(int userId, String date) {
        //TODO error handling for date
        LocalDate searchDate = LocalDate.parse(date);
        List<Order> orders = repo.findOrdersByUserId(userId);
        if (orders == null) {
            return null;
        }
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Order order : orders) {
            //TODO see if chronozone actually works for check
//            if(order.getDateTime().isAfter(ChronoZonedDateTime.from(searchDate))) {
//                BigDecimal value = order.getPrice().multiply(order.getNumberOfShares());
//                if (order.getTransactionType() == TransactionType.BUY) {
//                    totalValue = totalValue.add(value);
//                } else if (order.getTransactionType() == TransactionType.SELL) {
//                    totalValue = totalValue.subtract(value);
//                }
//            }
        }
        return totalValue;
    }
    public Result<Portfolio> updateCostBasisOnDividend(int userId, BigDecimal dividend) {
        Result<Portfolio> result = new Result<>();
        Portfolio portfolio = repo.findByUserId(userId);
        if (portfolio == null) {
            result.addMessage("User not found", ResultType.NOT_FOUND);
        }
        //TODO

        return result;
    }

    public BigDecimal calculateCapitalGainsTax(List<Order> orders, BigDecimal taxRate) {

        BigDecimal totalValue = BigDecimal.ZERO;
        for (Order order : orders) {
            BigDecimal value = order.getPrice().multiply(order.getNumberOfShares());
            if (order.getTransactionType() == TransactionType.SELL) {
                totalValue = totalValue.add(value);
            } else if (order.getTransactionType() == TransactionType.BUY) {
                totalValue = totalValue.subtract(value);
            }
        }

        return totalValue.multiply(taxRate);
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
