package learn.lavadonut.controllers;

import learn.lavadonut.domain.CurrencyService;
import learn.lavadonut.models.Currency;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService service;

    public CurrencyController(CurrencyService service) { this.service = service; }

    @GetMapping
    public List<Currency> findAll() { return service.findAll(); }

    //TODO: findById, add, update, deleteById
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int currencyId) {

    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Currency currency) {

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int currencyId, @RequestBody Currency currency) {

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int currencyId) {

    }

}
