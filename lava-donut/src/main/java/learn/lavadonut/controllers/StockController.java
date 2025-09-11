package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Stocks Api", description = "Endpoints for managing Stocks")
public class StockController {

    private final StockService service;

    public StockController(StockService service) { this.service = service; }

    @Operation(summary = "Get all Stocks")
    @GetMapping
    public List<Stock> findAll() { return service.findAll(); }

    @Operation(summary = "Get all Stocks by Industry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stocks of this industry are found!"),
            @ApiResponse(responseCode = "400", description = "industry is invalid"),
            @ApiResponse(responseCode = "404", description = "Stocks not found")
    })
    @GetMapping("/industry/{industry}")
    public ResponseEntity<Object> getStocksByIndustry(@PathVariable String industry){
        Result<List<Stock>> result = service.getStocksByIndustry(industry);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "Find a Stock by Ticker")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock of this ticker is found!"),
            @ApiResponse(responseCode = "400", description = "ticker is invalid"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<Object> findByTicker(@PathVariable String ticker){
        Result<Stock> result = service.findByTicker(ticker);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "Find a Stock by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock of this is is found!"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
    @GetMapping("/{stockId}")
    public ResponseEntity<Object> findById(@PathVariable int stockId){
        Stock stock = service.findById(stockId);
        if (stock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stock);
    }

    @Operation(summary = "ADMIN: Add a Stock")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Stock created"),
            @ApiResponse(responseCode = "400", description = "Invalid Stock")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> add(@RequestBody Stock stock) {
        Result<Stock> result = service.add(stock);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "ADMIN: Update a Stock")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Stock not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
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

    @Operation(summary = "ADMIN: Delete a Stock by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Stock deleted"),
            @ApiResponse(responseCode = "404", description = "Stock not found")
    })
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
