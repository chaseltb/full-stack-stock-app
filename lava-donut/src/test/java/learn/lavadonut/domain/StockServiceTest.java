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
    void shouldNotReturnStockWithNullIndustry(){  // UNHAPPY PATH
        Result<List<Stock>> result = service.getStocksByIndustry(null);

        assertFalse(result.isSuccess());
    }

    /**
     findAll Test!
     **/

    @Test
    void shouldFindById(){
        Stock expected = makeStock();
        when(repository.findById(2)).thenReturn(expected);

        Stock actual = service.findById(2);
        assertNotNull(actual);
    }

    /**
     add Test!
     **/


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