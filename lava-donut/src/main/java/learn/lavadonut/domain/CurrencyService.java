package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
import learn.lavadonut.models.Currency;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) { this.repository = repository; }

    public List<Currency> findAll() { return repository.findAll(); }

    public Currency findById(int currencyId){ return repository.findById(currencyId); }

    //TODO: add, update, delete, validate
    public Result<Currency> add(Currency currency){

    }

    public Result<Currency> update(Currency currency){

    }

    public Result<Currency> delete(int currencyId){

    }

    private Result<Currency> validate(Currency currency){
        Result<Currency> result = new Result<>();

        if(currency == null){

        }

        if(Validations.isNullOrBlank(currency.getName())){

        }

        if(Validations.isNullOrBlank(currency.getCode())){

        }

        if(currency.getValueToUsd().compareTo(BigDecimal.ZERO) != 1){

        }
        return result;
    }
}
