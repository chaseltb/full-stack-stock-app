package learn.lavadonut.data;

import learn.lavadonut.domain.ResultType;
import learn.lavadonut.models.Country;
import learn.lavadonut.models.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        country.setCode("US");

        Country actual = repository.findById(1);

        assertEquals(country.getId(), actual.getId());
        assertEquals(country.getName(), actual.getName());
        assertEquals(country.getCode(), actual.getCode());

        assertEquals(country.getCurrency().getId(), actual.getCurrency().getId());
        assertEquals(country.getCurrency().getName(), actual.getCurrency().getName());
        assertEquals(country.getCurrency().getCode(), actual.getCurrency().getCode());
        assertEquals(country.getCurrency().getValueToUsd(), actual.getCurrency().getValueToUsd());

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
        Currency currency = new Currency();
        currency.setId(2);
        currency.setCode("EUR");
        currency.setName("Euro");
        currency.setValueToUsd(new BigDecimal("1.17"));
        Country country = new Country(1, currency, "Federal Republic of Germany", "DE");

        country.setCode("IT");
        assertTrue(repository.update(country));

        country.setId(999);
        assertFalse(repository.update(country));
    }

    @Test
    void shouldDelete() {
        Country country = makeCountry();
        country.setId(4);
        assertEquals(ResultType.SUCCESS, repository.delete(4));

        assertEquals(ResultType.NOT_FOUND, repository.delete(4));

        assertEquals(ResultType.INVALID, repository.delete(1));
    }

    Country makeCountry() {
        Currency currency = new Currency();
        currency.setId(1);
        currency.setName("United States dollar");
        currency.setCode("USD");
        currency.setValueToUsd(new BigDecimal("1.0"));

        return new Country(1, currency, "United States of America", "USA");
    }
}