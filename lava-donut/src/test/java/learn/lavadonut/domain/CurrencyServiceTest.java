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

@SpringBootTest
class CurrencyServiceTest {

    @Autowired
    CurrencyService service;

    @MockBean
    CurrencyRepository repository;

    /**
     findAll Test!
     **/

    @Test
    void shouldFindAll(){
        List<Currency> expected = makeCurrencyList();

        when(repository.findAll()).thenReturn(expected);
        List<Currency> actual =  service.findAll();
        assertEquals(expected, actual);
    }

    /**
     findById Test!
     **/

    @Test
    void shouldFindById(){
        Currency expected = makeCurrency();

        when(repository.findById(4)).thenReturn(expected);
        Currency actual = service.findById(4);

        assertEquals(expected, actual);
    }

    /**
     add Tests!
     **/

    @Test
    void shouldAddWhenValid(){ // HAPPY PATH
        Currency expected = makeCurrency();
        Currency arg = makeCurrency();
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Currency> result = service.add(arg);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertTrue(expected.equals(result.getPayload())
                        && expected.getId() == result.getPayload().getId());
    }

    @Test
    void shouldNotAddWhenCurrencyIsNull(){ // UNHAPPY PATH
        Result<Currency> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenCurrencyNameIsNull(){
        Currency arg = makeCurrency();
        arg.setId(0);
        arg.setName(null);

        Result<Currency> result = service.add(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setName("");

        Result<Currency> blankResult = service.add(arg);

        assertEquals(ResultType.INVALID, blankResult.getType());

    }

    @Test
    void shouldNotAddWhenCurrencyCodeIsNull(){
        Currency arg = makeCurrency();
        arg.setId(0);
        arg.setCode(null);

        Result<Currency> result = service.add(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setCode("");

        Result<Currency> blankResult = service.add(arg);

        assertEquals(ResultType.INVALID, blankResult.getType());
    }

    @Test
    void shouldNotAddWhenCurrencyValueIsNotGreaterThanZero(){
        Currency arg = makeCurrency();
        arg.setId(0);
        arg.setValueToUsd(BigDecimal.ZERO);

        Result<Currency> result = service.add(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setValueToUsd(BigDecimal.valueOf(-1));

        Result<Currency> negativeResult = service.add(arg);

        assertEquals(ResultType.INVALID, negativeResult.getType());
    }

    @Test
    void shouldNotAddDuplicate(){
        Currency currency = makeCurrency();
        currency.setId(0);
        List<Currency> yen = List.of(makeCurrency());

        when(repository.findAll()).thenReturn(yen);

        Result<Currency> result = service.add(currency);

        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenIdIsGreaterThanZero(){
        Currency arg = makeCurrency();

        Result<Currency> result = service.add(arg);

        assertEquals(ResultType.INVALID, result.getType());
    }

    /**
     update Tests!
     **/

    @Test
    void shouldUpdateWhenValid(){ // HAPPY PATH
        Currency currency = makeCurrency();
        currency.setName("Alternate Yen");

        when(repository.update(currency)).thenReturn(true);
        Result<Currency> result = service.update(currency);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWhenCurrencyNull(){ //UNHAPPY PATH
        Result<Currency> result = service.update(null);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenCurrencyNameIsNull(){
        Currency arg = makeCurrency();
        arg.setName(null);

        Result<Currency> result = service.update(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setName("");

        Result<Currency> blankResult = service.update(arg);

        assertEquals(ResultType.INVALID, blankResult.getType());

    }

    @Test
    void shouldNotUpdateWhenCurrencyCodeIsNull(){
        Currency arg = makeCurrency();
        arg.setCode(null);

        Result<Currency> result = service.update(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setCode("");

        Result<Currency> blankResult = service.update(arg);

        assertEquals(ResultType.INVALID, blankResult.getType());
    }

    @Test
    void shouldNotUpdateWhenCurrencyValueIsNotGreaterThanZero(){
        Currency arg = makeCurrency();
        arg.setValueToUsd(BigDecimal.ZERO);

        Result<Currency> result = service.update(arg);

        assertEquals(ResultType.INVALID, result.getType());

        arg.setValueToUsd(BigDecimal.valueOf(-1));

        Result<Currency> negativeResult = service.update(arg);

        assertEquals(ResultType.INVALID, negativeResult.getType());
    }

    @Test
    void shouldNotUpdateDuplicate(){
        Currency currency = makeCurrency();
        List<Currency> yen = List.of(makeCurrency());

        when(repository.findAll()).thenReturn(yen);

        Result<Currency> result = service.update(currency);

        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenIdIsLessThanOne(){
        Currency arg = makeCurrency();
        arg.setId(0);

        Result<Currency> result = service.update(arg);

        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateNonExistentCurrency(){
        Currency arg = makeCurrency();

        when(repository.update(arg)).thenReturn(false);

        Result<Currency> result = service.update(arg);

        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    /**
     delete Tests!
     **/


    @Test
    void shouldDeleteWhenValid(){ //HAPPY PATH
        when(repository.delete(1)).thenReturn(true);

        Result<Currency> result = service.delete(1);

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteWhenNotFound(){ //UNHAPPY PATH
        when(repository.delete(999)).thenReturn(false);

        Result<Currency> result = service.delete(999);

        assertEquals(ResultType.NOT_FOUND, result.getType());
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
        currency.setId(4);

        return currency;
    }
}