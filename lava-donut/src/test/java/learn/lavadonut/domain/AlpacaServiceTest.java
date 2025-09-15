package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AlpacaServiceTest {
    @Autowired
    AlpacaService service;

    @MockBean
    StockRepository repo;

    @Test
    void shouldUpdatePrice() {
        Stock stock = new Stock();
        stock.setId(1);
        stock.setCurrentPrice(BigDecimal.ZERO);

        when(repo.findByTicker("AAPL")).thenReturn(stock);
        when(repo.update(stock)).thenReturn(true);
        Result<Stock> result = service.updateStockFromAlpaca("AAPL");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getPayload().getId());
        assertNotEquals(BigDecimal.ZERO, result.getPayload().getCurrentPrice());
    }

    @Test
    void shouldNotUpdateNullStock() {
        Result<Stock> result = service.updateStockFromAlpaca(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNonexistingStock() {
        when(repo.findByTicker("TEST")).thenReturn(null);
        Result<Stock> result = service.updateStockFromAlpaca("TEST");
        assertFalse(result.isSuccess());
    }

}