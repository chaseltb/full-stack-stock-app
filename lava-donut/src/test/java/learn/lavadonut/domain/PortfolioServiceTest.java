package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PortfolioServiceTest {

    @Autowired
    PortfolioService service;

    @MockBean
    PortfolioRepository repo;

    @MockBean
    StockRepository stockRepo;

    @MockBean
    OrderRepository orderRepo;

    @Test
    public void shouldFindAllStocksInPortfolio() {

        when(repo.findAllStocksInPortfolio(1)).thenReturn(List.of(new Stock()));
        List<Stock> stocks = service.findAllOwnedStocksInPortfolio(1);
        assertFalse(stocks.isEmpty());
    }

    @Test
    public void shouldNotFindNonexistingId() {
        when(repo.findPortfoliosByUserId(9999)).thenReturn(null);
        List<Portfolio> result =  service.findPortfoliosByUserId(9999);
        assertNull(result);
    }

    @Test
    public void shouldFindId() {
        Portfolio expected = new Portfolio();
        expected.setId(10);
        expected.setAccountType(AccountType.INVESTMENT);
        expected.setUserId(1);
        when(repo.findPortfoliosByUserId(1)).thenReturn(List.of(expected));
        List<Portfolio> result =  service.findPortfoliosByUserId(1);
        assertNotNull(result);
        assertEquals(result.get(0), expected);
    }

    @Test
    public void shouldUpdateAccountType() {
        Portfolio p = getTestPortfolio();
        p.setAccountType(AccountType.ROTH_IRA);
        when(repo.updateAccountType(p)).thenReturn(true);
        when(repo.findPortfoliosByUserId(1)).thenReturn(List.of(getTestPortfolio()));
        Result<Portfolio> result = service.updateAccountType(p);
        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNullAccountType() {
        Portfolio p = getTestPortfolio();
        p.setAccountType(null);
        Result<Portfolio> result = service.updateAccountType(p);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldGetPortfolioValue() {
        List<Order> orders = getTestOrders();
        when(repo.findOrdersByPortfolioId(1)).thenReturn(orders);
        when(stockRepo.findAll()).thenReturn(List.of(getTestStock()));
        Result<BigDecimal> result = service.getPortfolioValue(1, "2025-01-01");
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("1000"), result.getPayload());
    }

    @Test
    public void shouldUpdateCostBasisOnDividend() {

    }

    @Test
    void shouldCreatePortfolio() {
        Portfolio p = getTestPortfolio();
        p.setId(0);
        Portfolio expected = getTestPortfolio();
        expected.setId(1);
        when(repo.createPortfolio(p)).thenReturn(expected);
        when(repo.findPortfoliosByUserId(p.getUserId())).thenReturn(null);
        Result<Portfolio> result = service.createPortfolio(p);
        assertTrue(result.isSuccess());
        assertTrue(result.getPayload().getId() > 0);
    }

    @Test
    void shouldNotCreatePortfolioWithId() {
        Portfolio p = getTestPortfolio();
        p.setId(1);
        Result<Portfolio> result = service.createPortfolio(p);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreatePortfolioOfSameAccoutType() {
        Portfolio p = getTestPortfolio();
        p.setId(0);
        Portfolio dupe = getTestPortfolio();
        p.setId(10);
        when(repo.findPortfoliosByUserId(p.getUserId())).thenReturn(List.of(dupe));
        Result<Portfolio> result = service.createPortfolio(p);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldCalculateCapitalGainsTax() {

        Result<BigDecimal> result = service.calculateCapitalGainsTax(getTestOrders(),
                new BigDecimal("0.1"));
        assertTrue(result.isSuccess());
        //bought 100 for 10 each, sold 10 for 100 each so 900 in gains taxed at 10%
        assertEquals(new BigDecimal("90.0"), result.getPayload());

    }

    @Test
    void shouldAddPortfolioOrder() {
        Order order = getTestOrders().get(2);
        order.setId(0);
        Order expected = getTestOrders().get(2);
        when(orderRepo.findById(3)).thenReturn(order);
        when(orderRepo.add(order)).thenReturn(expected);
        when(repo.findPortfolioById(1)).thenReturn(getTestPortfolio());
        when(repo.addOrderToPortfolio(1, 3)).thenReturn(true);
        Result<Portfolio> result = service.addOrderToPortfolio(1, order);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldGetTotalSharesForEachStock() {
        List<Stock> stocks = getTestStocks();
        when(repo.findOrdersByPortfolioId(1)).thenReturn(getTestOrders());
        when(repo.findAllStocksInPortfolio(1)).thenReturn(stocks);
        Result<Map<Stock, BigDecimal>> result = service.getStockToTotalShares(1);
        assertTrue(result.isSuccess());
        assertFalse(result.getPayload().isEmpty());
        assertEquals(BigDecimal.valueOf(100), result.getPayload().get(stocks.get(0)));
    }

    @Test
    void shouldGetStockToCostBasis() {
        Portfolio p = getTestPortfolio();
        p.setStocks(getTestStocks());
        p.setOrders(getTestOrders());
        when(repo.findPortfolioById(1)).thenReturn(p);
        Result<Map<Stock, BigDecimal>> result = service.getCostBasisAllStocks(1);
        assertTrue(result.isSuccess());
        //bought 100 for 10, sold 10 for 100, then bought 10 for 80
        //should have 90 shares of 10, 10 of 80 = 900+800=1700/ (90+10) = 17 cost basis
        Map<Stock, BigDecimal> map = result.getPayload();
        assertNotNull(map);
        assertEquals(new BigDecimal("17.00"), map.get(p.getStocks().get(0)));

    }

    private List<Stock> getTestStocks() {
        List<Stock> stocks = new ArrayList<>();
        Stock s1 = getTestStock();
        Stock s2 = getTestStock();
        s2.setId(2);
        s2.setCurrentPrice(BigDecimal.valueOf(42));
        Stock s3 = getTestStock();
        s3.setId(3);
        s3.setCurrentPrice(BigDecimal.valueOf(1.15));
        stocks.add(s1);
        stocks.add(s2);
        stocks.add(s3);

        return stocks;
    }

    private List<Order> getTestOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1);
        order1.setTransactionType(TransactionType.BUY);
        order1.setStockId(1);
        order1.setNumberOfShares(new BigDecimal(100));
        order1.setDate(Date.valueOf("2025-01-01"));
        order1.setPrice(new BigDecimal(10));
        Order order2 = new Order();
        order2.setId(2);
        order2.setTransactionType(TransactionType.SELL);
        order2.setStockId(1);
        order2.setNumberOfShares(new BigDecimal(10));
        order2.setDate(Date.valueOf("2025-01-02"));
        order2.setPrice(new BigDecimal(100));
        Order order3 = new Order();
        order3.setId(3);
        order3.setTransactionType(TransactionType.BUY);
        order3.setStockId(1);
        order3.setNumberOfShares(new BigDecimal(10));
        order3.setDate(Date.valueOf("2025-09-02"));
        order3.setPrice(new BigDecimal(80));
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        return orders;
    }

    private Stock getTestStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(new BigDecimal(10));

        return stock;
    }

    private Portfolio getTestPortfolio() {
        Portfolio p = new Portfolio();
        p.setUserId(1);
        p.setAccountType(AccountType.INVESTMENT);
        p.setId(1);
        return p;
    }






}