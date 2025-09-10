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
import static org.mockito.Mockito.when;

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
        when(repository.findById(1)).thenReturn(country);
        Country actual = service.findById(1);
        assertEquals(country, actual);

        actual = service.findById(999);
        assertNull(actual);
    }

    // add
    @Test
    void shouldAdd() {
        Country expected = makeCountry();
        Country arg = makeCountry();
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Country> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullCountry() {
        Result<Country> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddCountryWithNullCurrency() {
        Country country = makeCountry();
        country.setCurrency(null);
        Result<Country> result = service.add(country);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddCountryWithNullName() {
        Country country = makeCountry();
        country.setName(null);
        Result<Country> result = service.add(country);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddCountryWithBlankName() {
        Country country = makeCountry();
        country.setName("");
        Result<Country> result = service.add(country);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddCountryWithNullCode() {
        Country country = makeCountry();
        country.setCode(null);
        Result<Country> result = service.add(country);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddCountryWithBlankCode() {
        Country country = makeCountry();
        country.setCode("");
        Result<Country> result = service.add(country);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddDuplicateCode() {
        // making a list with the duplicate
        Country country = makeCountry();
        when(repository.findAll()).thenReturn(List.of(country));

        Country duplicate = makeCountry();
        duplicate.setId(5);
        Result<Country> result = service.add(duplicate);

        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
    }

    // update
    @Test
    void shouldUpdate() {
        Country country = makeCountry();
        when(repository.update(country)).thenReturn(true);
        Result<Country> result = service.update(country);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertNotNull(result.getPayload());
        assertEquals(country, result.getPayload());
    }

    @Test
    void shouldNotUpdateMissingCountry() {
        Currency currency = new Currency();
        currency.setId(1);
        currency.setName("Euro");
        currency.setCode("ISO 4217");
        currency.setValueToUsd(new BigDecimal("1.17"));

        Country country = new Country(999, currency, "Italy", "ITA");
        when(repository.update(country)).thenReturn(false);
        Result<Country> result = service.update(country);

        System.out.println(result.getType());
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldDelete() {
        when(repository.delete(3)).thenReturn(true);
        assertTrue(service.delete(3));

        when(repository.delete(3)).thenReturn(false);
        assertFalse(service.delete(3));
    }

    Country makeCountry() {
        Currency currency = new Currency();
        currency.setId(1);
        currency.setName("United States dollar");
        currency.setCode("USD");
        currency.setValueToUsd(new BigDecimal("1.0"));

        return new Country(1, currency, "United States of America", "US");
    }
}