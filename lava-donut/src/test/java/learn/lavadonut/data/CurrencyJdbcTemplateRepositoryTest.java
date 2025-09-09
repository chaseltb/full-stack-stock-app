package learn.lavadonut.data;

import learn.lavadonut.models.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CurrencyJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    CurrencyJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAll() {
        List<Currency> currencies = repository.findAll();
        assertFalse(currencies.isEmpty());

        assertEquals(3, currencies.size());
    }
}