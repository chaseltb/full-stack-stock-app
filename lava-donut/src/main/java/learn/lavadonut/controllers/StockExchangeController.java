package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.lavadonut.domain.Result;
import learn.lavadonut.domain.StockExchangeService;
import learn.lavadonut.models.StockExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-exchange")
@CrossOrigin(origins = {"http://localhost:3000"})
@Tag(name = "Stock Exchange API", description = "Endpoints for managing stock exchanges")
public class StockExchangeController {
    private final StockExchangeService service;

    public StockExchangeController(StockExchangeService service) {
        this.service = service;
    }

    @Operation(summary = "Get all stock exchanges")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public List<StockExchange> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Find a stock exchange by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock exchange found"),
            @ApiResponse(responseCode = "404", description = "Stock exchange not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{stockExchangeId}")
    public ResponseEntity<Object> findById(@PathVariable int stockExchangeId) {
        StockExchange exchange = service.findById(stockExchangeId);
        if (exchange == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(exchange);
    }

    @Operation(summary = "Add a stock exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock exchange created"),
            @ApiResponse(responseCode = "400", description = "Invalid exchange input")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody StockExchange exchange) {
        Result<StockExchange> result = service.add(exchange);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Update a stock exchange")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock exchange updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Stock exchange not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{stockExchangeId}")
    public ResponseEntity<Object> update(@PathVariable int stockExchangeId, @RequestBody StockExchange exchange) {
        if (stockExchangeId != exchange.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<StockExchange> result = service.update(exchange);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a stock exchange by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Stock exchange deleted"),
            @ApiResponse(responseCode = "404", description = "Stock exchange not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{stockExchangeId}")
    public ResponseEntity<Object> deleteById(@PathVariable int stockExchangeId) {
        Result<Void> result= service.deleteById(stockExchangeId);
        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
