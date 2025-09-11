package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Find all Currencies")
    @ApiResponse(responseCode = "200", description = "Currencies found")
    @GetMapping
    public List<Currency> findAll() { return service.findAll(); }

    @Operation(summary = "Find a Currency by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency of this id is found!"),
            @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int currencyId) {
        Currency currency = service.findById(currencyId);
        if (currency == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(currency);
    }

    @Operation(summary = "ADMIN: Add a Currency")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Currency created"),
            @ApiResponse(responseCode = "400", description = "Invalid Currency")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Currency currency) {
        Result<Currency> result = service.add(currency);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "ADMIN: Update a Currency")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Currency not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
    @PutMapping("/{currencyId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

    @Operation(summary = "ADMIN: Delete a Currency by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Currency deleted"),
            @ApiResponse(responseCode = "404", description = "Currency not found")
    })
    @DeleteMapping("/{currencyId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> delete(@PathVariable int currencyId) {
        Result<Currency> result = service.delete(currencyId);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

}
