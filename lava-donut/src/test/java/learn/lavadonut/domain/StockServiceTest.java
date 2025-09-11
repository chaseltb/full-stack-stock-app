package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Country;
import learn.lavadonut.models.Stock;
import learn.lavadonut.models.StockExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockServiceTest {

    @Autowired
    StockService service;

    @MockBean
    StockRepository repository;

    /**
     findAll Test!
     **/

    @Test
    void shouldFindAll(){
        List<Stock> expected = List.of(makeStock());
        when(repository.findAll()).thenReturn(expected);

        List<Stock> actual = service.findAll();
        assertFalse(actual.isEmpty());
    }

    /**
     getStocksByIndustry Test!
     **/

    @Test
    void shouldGetStocksByIndustry(){ // HAPPY PATH
        List<Stock> expected = List.of(makeStock());
        when(repository.getStocksByIndustry("TEST-INDUSTRY")).thenReturn(expected);
        Result<List<Stock>> result = service.getStocksByIndustry("TEST-INDUSTRY");
        assertTrue(result.isSuccess());

        Stock actual = result.getPayload().get(0);
        assertEquals("TEST-INDUSTRY", actual.getIndustry());
    }

    @Test
    void shouldNotReturnStocksWithNullIndustry(){  // UNHAPPY PATH
        Result<List<Stock>> result = service.getStocksByIndustry(null);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotReturnStocksWithNoExistingIndustry(){
        List<Stock> expected = new ArrayList<>();
        when(repository.getStocksByIndustry("DOESNT-EXIST")).thenReturn(expected);
        Result<List<Stock>> result = service.getStocksByIndustry("DOESNT-EXIST");
        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    /**
     findAll Test!
     **/

    @Test
    void shouldFindById(){
        Stock expected = makeStock();
        when(repository.findById(1)).thenReturn(expected);

        Stock actual = service.findById(1);
        assertNotNull(actual);
    }

    /**
     findByTicker Test!
     **/

    @Test
    void shouldFindByTicker(){ // HAPPY PATH
        Stock expected = makeStock();
        when(repository.findByTicker("TEST-STOCK-TICKER")).thenReturn(expected);

        Result<Stock> result = service.findByTicker("TEST-STOCK-TICKER");

        assertTrue(result.isSuccess());
        assertEquals("TEST-STOCK-TICKER", result.getPayload().getTicker());
    }

    @Test
    void shouldNotFindNullTicker(){ // UNHAPPY PATH
        Result<Stock> result = service.findByTicker(null);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotFindNonExistingTicker(){
        Stock expected = null;
        when(repository.findByTicker("DOESNT-EXIST")).thenReturn(expected);
        Result<Stock> result = service.findByTicker("DOESNT-EXIST");
        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    /**
     add Test!
     **/

    @Test
    void shouldAdd(){ // HAPPY PATH
        Stock expected = makeStock();
        Stock arg = makeStock();
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Stock> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertTrue(expected.equals(arg));
    }
    @Test
    void shouldAddWhenIndustryIsNull(){
        Stock expected = makeStock();
        expected.setName(null);
        Stock arg = makeStock();
        arg.setName(null);
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Stock> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    Stock makeStock() {
        Stock stock = new Stock();

        stock.setId(1);
        stock.setName("TEST STOCK NAME");
        stock.setTicker("TEST-STOCK-TICKER");
        stock.setAssetType(AssetType.STOCK);
        stock.setIndustry("TEST-INDUSTRY");
        stock.setCurrentPrice(BigDecimal.valueOf(10.50));

        Country country = new Country();
        country.setId(2);
        stock.setCountry(country);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setId(2);
        stock.setStockExchange(stockExchange);

        return stock;
    }

}