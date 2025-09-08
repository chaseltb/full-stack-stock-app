package learn.lavadonut.data;

import learn.lavadonut.models.Country;
import learn.lavadonut.models.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CountryJdbcTemplateRepositoryTest {
    @Autowired
    CountryJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Country> countries = repository.findAll();
        assertNotNull(countries);
    }

    @Test
    void shouldFindById() {
        Country country = makeCountry();

        Country actual = repository.findById(1);
        assertEquals(country, actual);

        actual = repository.findById(999);
        assertNull(actual);
    }

    @Test
    void shouldAdd() {
        Country country = makeCountry();
        Country actual = repository.add(country);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdate() {
        Country country = makeCountry();
        country.setCode("IT");
        assertTrue(repository.update(country));

        assertFalse(repository.update(new Country()));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(1));
        assertFalse(repository.delete(1));
    }

    Country makeCountry() {
        Currency currency = new Currency();
        currency.setId(1);
        currency.setName("Euro");
        currency.setCode("ISO 4217");
        currency.setValueToUsd(new BigDecimal("1.17"));

        return new Country(1, currency, "Italy", "ITA");
    }
}