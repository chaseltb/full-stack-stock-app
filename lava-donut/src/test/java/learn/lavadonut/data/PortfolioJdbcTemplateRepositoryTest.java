package learn.lavadonut.data;

import learn.lavadonut.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PortfolioJdbcTemplateRepositoryTest {
    @Autowired
    PortfolioJdbcTemplateRepository repo;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindPortfolioByUserId() {
        List<Portfolio> portfolios = repo.findPortfoliosByUserId(1);
        assertNotNull(portfolios);
        assertEquals(1, portfolios.get(0).getId());
        assertEquals(1, portfolios.get(0).getUserId());
    }

    @Test
    void shouldFindAllStocksInPortfolio() {
        List<Stock> stocks = repo.findAllStocksInPortfolio(1);
        assertNotNull(stocks);
        assertFalse(stocks.isEmpty());
        //the order of these stocks is random, can no longer rely on position
        assertTrue(stocks.stream().anyMatch(s -> s.getId() == 1));
        assertTrue(stocks.stream().anyMatch(s -> s.getName().equalsIgnoreCase("AMERICAN AIRLINES GROUP INC")));
        assertTrue(stocks.stream().anyMatch(s -> s.getTicker().equalsIgnoreCase("TEST-TICKER1")));
        assertTrue(stocks.stream().anyMatch(s -> s.getIndustry().equalsIgnoreCase("airline and aviation")));
        assertTrue(stocks.stream().anyMatch(s -> s.getStockExchange() != null && s.getStockExchange().getId() == 1));
        assertTrue(stocks.stream().anyMatch(s -> s.getCountry() != null && s.getCountry().getId() == 1));
        assertTrue(stocks.stream().anyMatch(s -> s.getAssetType() == AssetType.STOCK));

    }

    @Test
    void shouldFindOrdersByPortfolioId() {
        List<Order> orders = repo.findOrdersByPortfolioId(1);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        //dont know order, just make sure it exists
        assertTrue(orders.stream().anyMatch(o -> o.getTransactionType() == TransactionType.BUY));
        assertTrue(orders.stream().anyMatch(o -> o.getNumberOfShares().equals(new BigDecimal("20.0"))));
        assertTrue(orders.stream().anyMatch(o -> o.getPrice().equals(new BigDecimal("2000.0"))));
        assertTrue(orders.stream().anyMatch(o -> o.getStockId() == 1));


    }

    @Test
    void shouldAddPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setAccountType(AccountType.RETIREMENT);
        portfolio.setUserId(1);

        portfolio = repo.createPortfolio(portfolio);
        assertTrue(portfolio.getId() > 0);
    }

    @Test
    void shouldUpdateAccountType() {
        Portfolio p = new Portfolio();
        p.setId(1);
        p.setUserId(1);
        p.setAccountType(AccountType.ROTH_IRA);
        assertTrue(repo.updateAccountType(p));
    }
}