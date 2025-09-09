package learn.lavadonut.domain;

import learn.lavadonut.data.PortfolioRepository;
import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        service.findAllOwnedStocksInPortfolio(1);
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
    public void shouldAddStockToPortfolio() {
        Stock stock = new Stock();
        stock.setAssetType(AssetType.STOCK);
        stock.setIndustry("Software");
        stock.setTicker("TEST");

//        Portfolio result = service.addWatchStockToPortfolio(1, stock);
    }

    @Test
    public void shouldDeleteWatchStockFromPortfolio() {
//        service.deleteWatchStockFromPortfolio(1,1);
    }

    @Test
    public void shouldUpdateAccountType() {
        service.updateAccountType(1, AccountType.RETIREMENT);
    }

    @Test
    public void shouldGetPortfolioValue() {
        service.getPortfolioValue(1, "2025-01-01");
    }

//    @Test
//    public void shouldSellStockFromPortfolio() {
//
//    }

    @Test
    public void shouldUpdateCostBasisOnDividend() {

    }

    @Test
    public void shouldCalculateCapitalGainsTax() {

    }



}