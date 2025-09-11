package learn.lavadonut.controllers;

import learn.lavadonut.domain.CurrencyService;
import learn.lavadonut.domain.Result;
import learn.lavadonut.models.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/currency")
public class CurrencyController {
    private final CurrencyService service;

    public CurrencyController(CurrencyService service) { this.service = service; }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public List<Currency> findAll() { return service.findAll(); }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public Currency findById(@PathVariable int currencyId) {
        return service.findById(currencyId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Currency currency) {
        Result<Currency> result = service.add(currency);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{currencyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> update(@PathVariable int currencyId, @RequestBody Currency currency) {
        if (currencyId != currency.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Currency> result = service.update(currency);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{currencyId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable int currencyId) {
        Result<Currency> result = service.delete(currencyId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

}
