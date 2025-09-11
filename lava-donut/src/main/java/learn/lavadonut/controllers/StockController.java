package learn.lavadonut.controllers;

import learn.lavadonut.domain.Result;
import learn.lavadonut.domain.StockService;
import learn.lavadonut.models.Stock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: turn into REST controller
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService service;

    public StockController(StockService service) { this.service = service; }

    @GetMapping
    public List<Stock> findAll() { return service.findAll(); }

    @GetMapping("/industry/{industry}")
    public ResponseEntity<Object> getStocksByIndustry(@PathVariable String industry){
        Result<List<Stock>> result = service.getStocksByIndustry(industry);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<Object> findByTicker(@PathVariable String ticker){
        Result<Stock> result = service.findByTicker(ticker);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/{stockId}")
    public Stock findById(@PathVariable int stockId){ return service.findById(stockId); }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Stock stock) {
        Result<Stock> result = service.add(stock);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{stockId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> update(@PathVariable int stockId, @RequestBody Stock stock){
        if (stockId != stock.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Stock> result = service.update(stock);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{stockId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable int stockId) {
        Result<Stock> result = service.delete(stockId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
