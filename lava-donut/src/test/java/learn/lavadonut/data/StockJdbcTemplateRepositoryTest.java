package learn.lavadonut.data;

import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Country;
import learn.lavadonut.models.Stock;
import learn.lavadonut.models.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 10;

    @Autowired
    StockJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    /**
     findAll Test!
     **/

    @Test
    void shouldFindAll() {
        List<Stock> stocks = repository.findAll();
        assertFalse(stocks.isEmpty());

        assertEquals(NEXT_ID - 1, stocks.size());
    }

    /**
     getStocksByIndustry Test!
     **/

    @Test
    void shouldFindByIndustry(){ // HAPPY PATH
        List<Stock> actual = repository.getStocksByIndustry("agriculture");

        assertFalse(actual.isEmpty());
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotFindNonexistentIndustry(){ // UNHAPPY PATH
        List<Stock> actual = repository.getStocksByIndustry("made up industry");

        assertTrue(actual.isEmpty());
    }

    /**
     findById Test!
     **/

    @Test
    void shouldFindById(){ // HAPPY PATH
        Stock actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(1, actual.getId());
        assertEquals("AMERICAN AIRLINES GROUP INC", actual.getName());
        assertEquals("TEST-TICKER1", actual.getTicker());
        assertTrue(actual.getAssetType().getName().equalsIgnoreCase("STOCK"));
        assertEquals("airline and aviation", actual.getIndustry());
        assertEquals("12.915", actual.getCurrentPrice().toString());
        assertEquals(1, actual.getStockExchange().getId());
        assertEquals(1, actual.getCountry().getId());
    }

    @Test
    void shouldNotFindNonExistentId(){ // UNHAPPY PATH
        Stock actual = repository.findById(999);
        assertNull(actual);
    }

    /**
     add Test!
     **/

    @Test
    void shouldAdd(){ // HAPPY PATH
        Stock stock = makeStock();
        Stock actual = repository.add(stock);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getId());
    }

    @Test
    void shouldAddWithNullIndustry(){
        Stock stock = makeStock();
        stock.setIndustry(null);

        Stock actual = repository.add(stock);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getId());
    }

    @Test
    void shouldNotAddWithNullStock(){ // UNHAPPY PATH
        Stock stock = null;
        Stock actual = repository.add(stock);
        assertNull(actual);
    }

    /**
     update Test!
     **/

    @Test
    void shouldUpdate(){ // HAPPY PATH
        Stock stock = makeStock();
        stock.setId(3);
        assertTrue(repository.update(stock));
    }

    @Test
    void shouldNotUpdateWithInvalidId(){ // UNHAPPY PATH
        Stock stock = makeStock();
        stock.setId(999);
        assertFalse(repository.update(stock));
    }

    /**
     delete Test!
     **/

    @Test
    void shouldDelete(){ // HAPPY PATH
        int validId = 2;
        assertTrue(repository.deleteById(validId));
    }

    @Test
    void shouldNotDeleteWithInvalidId(){ // UNHAPPY PATH
        int invalidId = 999;
        assertFalse(repository.deleteById(invalidId));
    }

    private Stock makeStock(){
        Stock stock = new Stock();

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