package learn.lavadonut.data;

import learn.lavadonut.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
        Portfolio portfolio = repo.findByUserId(1);
        assertNotNull(portfolio);
        assertEquals(1, portfolio.getId());
        assertEquals(AccountType.RETIREMENT, portfolio.getAccountType());
        assertEquals(1, portfolio.getUserId());
    }

    @Test
    void shouldFindAllStocksInPortfolio() {
        List<Stock> stocks = repo.findAllStocksInPortfolio(1);
        assertNotNull(stocks);
        assertEquals(1, stocks.get(0).getId());
        assertEquals("AMERICAN AIRLINES GROUP INC", stocks.get(0).getName());
        assertEquals("TEST-TICKER1", stocks.get(0).getTicker());
        assertEquals("airline and aviation", stocks.get(0).getIndustry());
        assertEquals(1, stocks.get(0).getStockExchange().getId());
        assertEquals(1, stocks.get(0).getCountry().getId());
        assertEquals(AssetType.STOCK, stocks.get(0).getAssetType());
    }

    @Test
    void shouldFindOrdersByUserId() {
        List<Order> orders = repo.findOrdersByUserId(1);
        assertNotNull(orders);
        assertEquals(1, orders.get(0).getId());
        assertEquals(TransactionType.BUY, orders.get(0).getTransactionType());
        assertEquals(new BigDecimal("20"), orders.get(0).getNumberOfShares());
        assertEquals(new BigDecimal("12.915"), orders.get(0).getPrice());
        assertEquals(1, orders.get(0).getStockId());

    }

    @Test
    void shouldUpdateAccountType() {
        assertTrue(repo.updateAccountType(1, AccountType.INVESTING));
    }
}