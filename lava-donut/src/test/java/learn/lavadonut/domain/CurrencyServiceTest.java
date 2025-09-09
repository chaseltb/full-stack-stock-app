package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
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
class CurrencyServiceTest {

    @Autowired
    CurrencyService service;

    @MockBean
    CurrencyRepository repository;

    @Test
    void shouldFindAll(){
        List<Currency> expected = makeCurrencyList();

        when(repository.findAll()).thenReturn(expected);
        List<Currency> actual =  service.findAll();
        assertEquals(expected, actual);
    }

    private List<Currency> makeCurrencyList(){
        // 1: ('United States dollar', 'USD', 1.0)
        // 2: (2, 'Euro', 'EUR', 1.17)
        // 3: (3, 'Chinese Yuan', 'CNY', 0.14)

        Currency dollar = new Currency();

        dollar.setId(1);
        dollar.setName("United States dollar");
        dollar.setCode("USD");
        dollar.setValueToUsd(BigDecimal.valueOf(1.0));

        Currency euro = new Currency();

        euro.setId(2);
        euro.setName("Euro");
        euro.setCode("EUR");
        euro.setValueToUsd(BigDecimal.valueOf(1.17));

        Currency yuan = new Currency();

        yuan.setId(3);
        yuan.setName("Chinese Yuan");
        yuan.setCode("CNY");
        yuan.setValueToUsd(BigDecimal.valueOf(0.14));

        return List.of(dollar, euro, yuan);
    }

    private Currency makeCurrency(){
        Currency currency = new Currency();
        currency.setName("Yen");
        currency.setCode("JPY");
        currency.setValueToUsd(BigDecimal.valueOf(0.0068));

        return currency;
    }
}