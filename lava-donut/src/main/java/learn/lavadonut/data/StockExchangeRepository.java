package learn.lavadonut.data;

import learn.lavadonut.models.StockExchange;

import java.util.List;

public interface StockExchangeRepository {
    List<StockExchange> findAll();

    StockExchange findById(int id);

    StockExchange add(StockExchange exchange);

    boolean update(StockExchange exchange);

    boolean deleteById(int stockExchangeId);
}
