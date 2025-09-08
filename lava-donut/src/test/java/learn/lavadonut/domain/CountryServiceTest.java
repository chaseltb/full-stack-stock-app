package learn.lavadonut.domain;

import learn.lavadonut.data.CountryRepository;
import learn.lavadonut.models.Country;
import learn.lavadonut.models.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CountryServiceTest {
    @Autowired
    CountryService service;

    @MockBean
    CountryRepository repository;

    @Test
    void shouldFindAll() {
        List<Country> countries = service.findAll();
        assertNotNull(countries);
    }

    @Test
    void shouldFindById() {
        Country country = makeCountry();

        Country actual = service.findById(1);
        assertEquals(country, actual);

        actual = service.findById(999);
        assertNull(actual);
    }

    @Test
    void shouldDelete() {
        assertTrue(service.delete(1));
        assertFalse(service.delete(1));
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