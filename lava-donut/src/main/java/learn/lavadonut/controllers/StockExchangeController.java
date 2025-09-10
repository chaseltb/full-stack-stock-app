package learn.lavadonut.controllers;

import learn.lavadonut.domain.StockExchangeService;
import learn.lavadonut.models.StockExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class StockExchangeController {
    private final StockExchangeService service;

    public StockExchangeController(StockExchangeService service) {
        this.service = service;
    }

    @GetMapping
    public List<StockExchange> findAll() {
        return service.findAll();
    }

    @GetMapping("/{stockExchangeId}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        return null;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody StockExchange exchange) {
        return null;
    }

    @PutMapping("/{stockExchangeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> update(@PathVariable int stockExchangeId) {
        return null;
    }

    @DeleteMapping("/{stockExchangeId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable int stockExchangeId) {
        return null;
    }
}
