package learn.lavadonut.data;

import learn.lavadonut.models.Stock;

import java.util.List;

public interface StockRepository {
    List<Stock> findAll();

    List<Stock> getStocksByIndustry(String industry);

    Stock findById(int stockId);

    Stock findByTicker(String ticker);

    Stock add(Stock stock);

    boolean update(Stock stock);

    boolean deleteById(int stockId);

    int getUsageCount(int stockId);
}
