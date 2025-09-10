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

    @Test
    void shouldFindAll() {
        List<Stock> stocks = repository.findAll();
        assertFalse(stocks.isEmpty());

        assertEquals(NEXT_ID - 1, stocks.size());
    }
}