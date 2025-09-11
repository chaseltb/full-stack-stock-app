package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PortfolioServiceTest {

    @Autowired
    PortfolioService service;

    @MockBean
    PortfolioRepository repo;

    @Test
    public void shouldFindAllStocksInPortfolio() {

        when(repo.findAllStocksInPortfolio(1)).thenReturn(List.of(new Stock()));
        List<Stock> stocks = service.findAllOwnedStocksInPortfolio(1);
        assertFalse(stocks.isEmpty());
    }

    @Test
    public void shouldNotFindNonexistingId() {
        when(repo.findByUserId(9999)).thenReturn(null);
        Portfolio result =  service.findByUserId(9999);
        assertNull(result);
    }

    @Test
    public void shouldFindId() {
        Portfolio expected = new Portfolio();
        expected.setId(10);
        expected.setAccountType(AccountType.INVESTING);
        expected.setUserId(1);
        when(repo.findByUserId(1)).thenReturn(expected);
        Portfolio result =  service.findByUserId(1);
        assertNotNull(result);
        assertEquals(result, expected);
    }

    @Test
    public void shouldUpdateAccountType() {
        Result<Portfolio> result = service.updateAccountType(1, AccountType.RETIREMENT);
        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNullAccountType() {
        Result<Portfolio> result = service.updateAccountType(1, null);
        assertTrue(result.isSuccess());
    }

    @Test
    public void shouldGetPortfolioValue() {
        List<Order> orders = getTestOrders();
        when(repo.findOrdersByUserId(1)).thenReturn(orders);
        Result<BigDecimal> result = service.getPortfolioValue(1, "2025-01-01");
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("1000"), result.getPayload());
    }

    @Test
    public void shouldUpdateCostBasisOnDividend() {

    }

    @Test
    public void shouldCalculateCapitalGainsTax() {

        Result<BigDecimal> result = service.calculateCapitalGainsTax(getTestOrders(),
                new BigDecimal("0.1"));
        assertTrue(result.isSuccess());
        //bought 100 for 10 each, sold 10 for 100 each so 900 in gains taxed at 10%
        assertEquals(new BigDecimal("90"), result.getPayload());

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

    public Stock getTestStock() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(new BigDecimal(10));

        return stock;
    }

//    @Test
//    public void shouldSellStockFromPortfolio() {
//
//    }
//    @Test
//    public void shouldDeleteWatchStockFromPortfolio() {
//        service.deleteWatchStockFromPortfolio(1,1);
//    }
//    @Test
//    public void shouldAddStockToPortfolio() {
//        Stock stock = new Stock();
//        stock.setAssetType(AssetType.STOCK);
//        stock.setIndustry("Software");
//        stock.setTicker("TEST");
//
//        Portfolio result = service.addWatchStockToPortfolio(1, stock);
//    }




}