package learn.lavadonut.domain;

import learn.lavadonut.data.StockExchangeRepository;
import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.Stock;
import learn.lavadonut.models.StockExchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class StockExchangeServiceTest {

    @Autowired
    StockExchangeService service;

    @MockBean
    StockExchangeRepository repo;

    @MockBean
    StockRepository stockRepo;


    @Test
    public void shouldFindAll() {
        StockExchange se1 = getTestStockExchange();
        se1.setId(1);
        StockExchange se2 = getTestStockExchange();
        se2.setId(2);
        List<StockExchange> exchanges = new ArrayList<>();
        exchanges.add(se1);
        exchanges.add(se2);
        when(repo.findAll()).thenReturn(exchanges);
        List<StockExchange> result = service.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());

    }

    @Test
    public void shouldFindById() {
        StockExchange se = getTestStockExchange();
        se.setId(1);
        when(repo.findById(1)).thenReturn(se);
        StockExchange result = service.findById(1);
        assertNotNull(result);
        assertEquals(result, se);
    }

    @Test
    public void shouldNotFindNonexistingId() {
        when(repo.findById(999)).thenReturn(null);
        StockExchange result = service.findById(999);
        assertNull(result);
    }

    @Test
    public void shouldAdd() {
        StockExchange se = getTestStockExchange();
        StockExchange expected = getTestStockExchange();
        expected.setId(4);
        when(repo.add(se)).thenReturn(expected);
        when(repo.findAll()).thenReturn(List.of());
        Result<StockExchange> result = service.add(se);
        assertTrue(result.isSuccess());

    }

    @Test
    public void shouldNotAddDuplicate() {
        StockExchange se = getTestStockExchange();
        StockExchange dupe = getTestStockExchange();
        dupe.setId(1);
        when(repo.findAll()).thenReturn(List.of(dupe));
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddWithId() {
        StockExchange se = getTestStockExchange();
        se.setId(1);
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddNullName() {
        StockExchange se = getTestStockExchange();
        se.setName(null);
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddBlankName() {
        StockExchange se = getTestStockExchange();
        se.setName("");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddLongName() {
        StockExchange se = getTestStockExchange();
        se.setName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+
                "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddNullCode() {
        StockExchange se = getTestStockExchange();
        se.setCode(null);
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddBlankCode() {
        StockExchange se = getTestStockExchange();
        se.setCode("");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddLongCode() {
        StockExchange se = getTestStockExchange();
        se.setCode("AAAAAAAAAAAAAAAA");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddInvalidCode() {
        StockExchange se = getTestStockExchange();
        se.setCode("KQR-TGM!");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddNullTimezone() {
        StockExchange se = getTestStockExchange();
        se.setTimeZone(null);
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddInvalidTimezone() {
        StockExchange se = getTestStockExchange();
        se.setTimeZone("Not a zone");
        Result<StockExchange> result = service.add(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldUpdate() {
        StockExchange se = getUpdateTestStockExchange();
        se.setId(1);
        se.setName("Luxembourg Bond Exchange");
        se.setCode("LBE");
        se.setTimeZone("Europe/Luxembourg");

        when(repo.update(se)).thenReturn(true);
        Result<StockExchange> result = service.update(se);
        assertTrue(result.isSuccess());

    }

    @Test
    public void shouldNotUpdateDuplicate() {
        StockExchange se = getUpdateTestStockExchange();
        StockExchange dupe = getUpdateTestStockExchange();
        se.setId(1);
        dupe.setId(2);
        when(repo.findAll()).thenReturn(List.of(dupe));
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNoId() {
        StockExchange se = getUpdateTestStockExchange();
        se.setId(0);
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNullName() {
        StockExchange se = getUpdateTestStockExchange();
        se.setName(null);
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateBlankName() {
        StockExchange se = getUpdateTestStockExchange();
        se.setName("");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateLongName() {
        StockExchange se = getUpdateTestStockExchange();
        se.setName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"+
                "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNullCode() {
        StockExchange se = getUpdateTestStockExchange();
        se.setCode(null);
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateBlankCode() {
        StockExchange se = getUpdateTestStockExchange();
        se.setCode("");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateLongCode() {
        StockExchange se = getUpdateTestStockExchange();
        se.setCode("AAAAAAAAAAAAAAAA");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateInvalidCode() {
        StockExchange se = getUpdateTestStockExchange();
        se.setCode("KQR-TGM!");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateNullTimezone() {
        StockExchange se = getUpdateTestStockExchange();
        se.setTimeZone(null);
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateInvalidTimezone() {
        StockExchange se = getUpdateTestStockExchange();
        se.setTimeZone("Not a zone");
        Result<StockExchange> result = service.update(se);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldDelete() {
        when(repo.deleteById(1)).thenReturn(true);
        when(stockRepo.findAll()).thenReturn(null);
        Result<Void> result = service.deleteById(1);
        assertTrue(result.isSuccess());

    }

    @Test
    public void shouldNotDeleteExchangeWithStocks() {
        when(repo.deleteById(1)).thenReturn(true);
        Stock testStock = new Stock();
        StockExchange se = getTestStockExchange();
        se.setId(1);
        testStock.setStockExchange(se);
        when(stockRepo.findAll()).thenReturn(List.of(testStock));
        Result<Void> result = service.deleteById(1);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotDeleteNonexisting() {
        when(repo.deleteById(999)).thenReturn(false);
        when(stockRepo.findAll()).thenReturn(null);
        Result<Void> result = service.deleteById(999);
        assertFalse(result.isSuccess());
    }

    private StockExchange getTestStockExchange() {
        StockExchange se = new StockExchange();
        se.setName("Taiwan Stock Exchange");
        se.setCode("TSE");
        se.setTimeZone("Europe/London");
        return se;
    }

    private StockExchange getUpdateTestStockExchange() {
        StockExchange se = getTestStockExchange();
        se.setId(1);
        return se;
    }

}