package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Country API", description = "Endpoints for managing countries")
public class CountryController {
    private CountryService service;

    public CountryController(CountryService service) {
        this.service = service;
    }

    @Operation(summary = "Find all countries")
    @ApiResponse(responseCode = "200", description = "Countries found")
    @GetMapping
    public ResponseEntity<List<Country>> findAll() {
        List<Country> countries = service.findAll();
        return new ResponseEntity<>(countries, HttpStatus.OK); // 200
    }

    @Operation(summary = "Find a country by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Country> findById(@PathVariable int id) {
        Country country = service.findById(id);
        if (country == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(country, HttpStatus.OK); // 200
    }

    @Operation(summary = "Add a country")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Country created"),
            @ApiResponse(responseCode = "400", description = "Invalid country")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Country country) {
        Result<Country> result = service.add(country);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "Update a country")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country updated"),
            @ApiResponse(responseCode = "400", description = "Invalid country"),
            @ApiResponse(responseCode = "404", description = "Country not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
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

    @Operation(summary = "Delete a country")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country deleted"),
            @ApiResponse(responseCode = "400", description = "Country in use"),
            @ApiResponse(responseCode = "404", description = "Country not found")
    })
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
