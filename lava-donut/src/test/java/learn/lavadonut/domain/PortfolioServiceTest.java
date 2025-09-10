package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
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
//        List<Order> orders = getTestOrders();
//        when(repo.findOrdersByUserId(1)).thenReturn(orders);
//        BigDecimal result = service.getPortfolioValue(1, "2025-01-01");
//        assertEquals(new BigDecimal("1000"), result);
    }

    @Test
    public void shouldUpdateCostBasisOnDividend() {

    }

    @Test
    public void shouldCalculateCapitalGainsTax() {
//
//        BigDecimal result = service.calculateCapitalGainsTax(getTestOrders(),
//                new BigDecimal("0.1"));
//        assertEquals(new BigDecimal("100"), result);

    }

//    private List<Order> getTestOrders() {
//        List<Order> orders = new ArrayList<>();
//        Order order1 = new Order(1, TransactionType.BUY, 1, new BigDecimal(100),
//                ZonedDateTime.of(LocalDate.of(2025, 1, 1), LocalTime.NOON, ZoneId.of("GMT")),
//                new BigDecimal(10), 1);
//        Order order2 = new Order(1, TransactionType.SELL, 1, new BigDecimal(10),
//                ZonedDateTime.of(LocalDate.of(2025, 1, 2), LocalTime.NOON, ZoneId.of("GMT")),
//                new BigDecimal(100), 1);
//        Order order3 = new Order(1, TransactionType.BUY, 1, new BigDecimal(10),
//                ZonedDateTime.of(LocalDate.of(2025, 9, 1), LocalTime.NOON, ZoneId.of("GMT")),
//                new BigDecimal(80), 1);
//        orders.add(order1);
//        orders.add(order2);
//        orders.add(order3);
//        return orders;
//    }

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
