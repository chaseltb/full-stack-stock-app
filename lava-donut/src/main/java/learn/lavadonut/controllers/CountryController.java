package learn.lavadonut.controllers;

import learn.lavadonut.domain.CountryService;
import learn.lavadonut.domain.Result;
import learn.lavadonut.models.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring DI and MVC
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/country") // Base URL
public class CountryController {
    private CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Country>> findAll() {
        List<Country> countries = service.findAll();
        return new ResponseEntity<>(countries, HttpStatus.OK); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable int id) {
        Country country = service.findById(id);
        if (country == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(country, HttpStatus.OK); // 200
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Country country) {
        Result<Country> result = service.add(country);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Country country) {
        // validate id
        if (id != country.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Country> result = service.update(country);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        Result<Country> result = service.delete(id);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}
