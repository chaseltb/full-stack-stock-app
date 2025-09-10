package learn.lavadonut.data;

import learn.lavadonut.models.StockExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockExchangeJdbcTemplateRepositoryTest {

    @Autowired
    StockExchangeJdbcTemplateRepository repo;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<StockExchange> exchanges = repo.findAll();
        assertNotNull(exchanges);
    }

    @Test
    void shouldFindById() {

        StockExchange exchange = repo.findById(1);
        assertEquals(1, exchange.getId());
        assertEquals("New York Stock Exchange", exchange.getName());
        assertEquals("NYSE", exchange.getCode());
        assertEquals(-5, exchange.getTimeZone());
    }

    @Test
    void shouldAdd() {
        StockExchange se = getTestStockExchange();
        StockExchange actual = repo.add(se);
        assertNotNull(se);
        assertEquals(4, actual.getId());
    }

    @Test
    void shouldUpdate() {
        StockExchange se = getTestStockExchange();
        se.setId(3);
        assertTrue(repo.update(se));
    }

    @Test
    void shouldNotUpdateNonexistingId() {
        StockExchange se = getTestStockExchange();
        se.setId(9999);
        assertFalse(repo.update(se));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repo.deleteById(2));
    }
    @Test
    void shouldNotDeleteNonExistingId() {
        assertFalse(repo.deleteById(9999));
    }

    private StockExchange getTestStockExchange() {
        StockExchange se = new StockExchange();
        se.setName("London Stock Exchange");
        se.setCode("LSE");
        se.setTimeZone("Europe/London");
        return se;
    }


}