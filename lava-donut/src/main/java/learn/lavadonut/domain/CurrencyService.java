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
            result.addMessage("currency cannot be null!", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(currency.getName())){
            result.addMessage("name is required!", ResultType.INVALID);
        }

        if(Validations.isNullOrBlank(currency.getCode())){
            result.addMessage("currency code is required! EX: 'USD'", ResultType.INVALID);
        }

        if(currency.getValueToUsd().compareTo(BigDecimal.ZERO) != 1){
            result.addMessage("value to USD is required!", ResultType.INVALID);
        }

        List<Currency> allCurrency = repository.findAll();

        if((!allCurrency.isEmpty()) && result.isSuccess()){
            for(Currency c : allCurrency){
                if(currency.equals(c) && currency.getId() != c.getId()){
                    result.addMessage("Duplicate currencies are not allowed", ResultType.INVALID);
                }
            }
        }

        return result;
    }
}
