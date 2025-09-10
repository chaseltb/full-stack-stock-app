package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Country;
import learn.lavadonut.models.Stock;
import learn.lavadonut.models.StockExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockServiceTest {

    @Autowired
    StockService service;

    @MockBean
    StockRepository repository;


    Stock makeStock() {
        Stock stock = new Stock();

        stock.setId(1);
        stock.setName("TEST STOCK NAME");
        stock.setTicker("TEST-STOCK-TICKER");
        stock.setAssetType(AssetType.STOCK);
        stock.setIndustry("TEST-INDUSTRY");
        stock.setCurrentPrice(BigDecimal.valueOf(10.50));

        Country country = new Country();
        country.setId(2);
        stock.setCountry(country);

        StockExchange stockExchange = new StockExchange();
        stockExchange.setId(2);
        stock.setStockExchange(stockExchange);

        return stock;
    }

}