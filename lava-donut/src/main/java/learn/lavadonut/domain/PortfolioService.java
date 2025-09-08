package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.models.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        repo.findAllStocksInPortfolio(userId);
    }
    public Result<Portfolio> updateAccountType(int userId, AccountType accountType) {

    }
    public BigDecimal getPortfolioValue(int userId, String date) {

    }
    public Result<Portfolio> updateCostBasisOnDividend(int userId, BigDecimal dividend) {

    }

    public BigDecimal calculateCapitalGainsTax(List<Order> orders) {

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
