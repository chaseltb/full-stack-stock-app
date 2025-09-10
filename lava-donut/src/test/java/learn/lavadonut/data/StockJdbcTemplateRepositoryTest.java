package learn.lavadonut.data;

import learn.lavadonut.models.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
}