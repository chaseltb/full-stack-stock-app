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
        Result<Currency> result = validate(currency);
        if (!result.isSuccess() ){
            return result;
        }

        if (currency.getId() != 0){
            result.addMessage("currency cannot be set for 'add' operation!", ResultType.INVALID);
            return result;
        }

        currency = repository.add(currency);
        result.setPayload(currency);
        return result;
    }

    public Result<Currency> update(Currency currency){
        Result<Currency> result = validate(currency);
        if(!result.isSuccess()) {
            return result;
        }

        if (currency.getId() <= 0) {
            result.addMessage("currency cannot be set for 'update' operation!", ResultType.INVALID);
            return result;
        }

        if (!repository.update(currency)){
            String error = String.format("currency: %s, could not be found!",
                    currency.getId());
            result.addMessage(error, ResultType.NOT_FOUND);
        }

        return result;
    }

    //TODO: may need to check if being used by other objects, come back to later
    public Result<Currency> delete(int currencyId){
        Result<Currency> result = new Result<>();

        if (!repository.delete(currencyId)){
            String error = String.format("currency: %s, could not be found!",
                    currencyId);
            result.addMessage(error, ResultType.NOT_FOUND);
        }

        return result;
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
                if(currency.equals(c)){
                    result.addMessage("Duplicate currencies are not allowed", ResultType.INVALID);
                }
            }
        }

        return result;
    }
}
