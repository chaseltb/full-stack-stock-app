package learn.lavadonut.data;

import learn.lavadonut.models.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    CurrencyJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    /**
     findAll Test!
     **/

    @Test
    void shouldFindAll() {
        List<Currency> currencies = repository.findAll();
        assertFalse(currencies.isEmpty());

        assertEquals(3, currencies.size());
    }

    /**
     findById Tests!
     **/

    @Test
    void shouldFindById() { // HAPPY PATH
        // 1: ('United States dollar', 'USD', 1.0)
        // 2: (2, 'Euro', 'EUR', 1.17)
        // 3: (3, 'Chinese Yuan', 'CNY', 0.14)

        Currency actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals("United States dollar", actual.getName());
        assertEquals("USD", actual.getCode());
        assertEquals(BigDecimal.valueOf(1.0), actual.getValueToUsd());

        actual = repository.findById(2);
        assertNotNull(actual);
        assertEquals("Euro", actual.getName());
        assertEquals("EUR", actual.getCode());
        assertEquals(BigDecimal.valueOf(1.17), actual.getValueToUsd());

        actual = repository.findById(3);
        assertNotNull(actual);
        assertEquals("Chinese Yuan", actual.getName());
        assertEquals("CNY", actual.getCode());
        assertEquals(BigDecimal.valueOf(0.14), actual.getValueToUsd());
    }

    @Test
    void shouldNotFindNonExistentId(){ // UNHAPPY PATH
        int nonExistentId = 999;

        Currency actual = repository.findById(nonExistentId);

        assertNull(actual);
    }

    /**
     add Tests!
     **/

    @Test
    void shouldAdd(){ // HAPPY PATH
        Currency currency = new Currency();
        currency.setName("Yen");
        currency.setCode("JPY");
        currency.setValueToUsd(BigDecimal.valueOf(0.0068));

        Currency actual = repository.add(currency);

        assertEquals(NEXT_ID, actual.getId());
    }

    @Test
    void shouldNotAddWithNullCurrency(){ // UNHAPPY PATH
        Currency currency = null;

        Currency actual = repository.add(currency);

        assertNull(actual);
    }

    /**
     update Tests!
     **/

    @Test
    void shouldUpdate(){ // HAPPY PATH
        Currency currency = new Currency();
        currency.setName("Yen");
        currency.setCode("JPY");
        currency.setValueToUsd(BigDecimal.valueOf(0.0068));
        currency.setId(2);

        assertTrue(repository.update(currency));
    }

    @Test
    void shouldNotUpdateWithInvalidId(){ //UNHAPPY PATH
        Currency currency = new Currency();
        currency.setName("Yen");
        currency.setCode("JPY");
        currency.setValueToUsd(BigDecimal.valueOf(0.0068));
        currency.setId(NEXT_ID + 1);

        assertFalse(repository.update(currency));
    }

    /**
     delete Tests!
     **/

    @Test
    void shouldDelete(){ // HAPPY PATH
        int validId = 1;

        assertTrue(repository.delete(validId));
    }

    @Test
    void shouldNotDeleteWithInvalidId(){ // UNHAPPY PATH
        int invalidId = 999;

        assertFalse(repository.delete(invalidId));
    }
}