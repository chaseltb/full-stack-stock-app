package learn.lavadonut.controllers;

import learn.lavadonut.domain.CountryService;
import learn.lavadonut.models.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring DI and MVC
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/order") // Base URL
public class CountryController {
    private CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Country>> findAll() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable int id) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Country country) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Country country) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        return null;
    }
}
