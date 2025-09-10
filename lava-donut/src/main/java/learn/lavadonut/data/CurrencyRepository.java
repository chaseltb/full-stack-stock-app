package learn.lavadonut.data;

import learn.lavadonut.models.Currency;

import java.util.List;

public interface CurrencyRepository {

    List<Currency> findAll();

    Currency findById(int currencyId);

    Currency add (Currency currency);

    boolean update (Currency currency);

    boolean delete (int currencyId);

    int getUsageCount(int currencyId);
}
