package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.lavadonut.domain.PortfolioService;
import learn.lavadonut.domain.Result;
import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/portfolio")
@Tag(name = "Portfolio API", description = "Endpoints for managing user portfolios")
public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @Operation(summary = "Find portfolio by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio found"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> findByUserId(@PathVariable int userId) {
        Portfolio portfolio = service.findByUserId(userId);
        if(portfolio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(portfolio);
    }

    @Operation(summary = "Find all stocks in a users portfolio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio Stocks"),
            @ApiResponse(responseCode = "404", description = "Portfolio Stocks not found")
    })
    @GetMapping("/{userId}/stocks")
    public ResponseEntity<List<Stock>> findAllStocksInPortfolio(@PathVariable int userId) {
        List<Stock> stocks = service.findAllOwnedStocksInPortfolio(userId);
        if (stocks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stocks);
    }

    @Operation(summary = "Get the value of a portfolio at a date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "The Value of the portfolio"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found")
    })
    @GetMapping("/{userId}/value")
    public ResponseEntity<BigDecimal> getPortfolioValue(@PathVariable int userId, @RequestParam String date) {
        Result<BigDecimal> result = service.getPortfolioValue(userId, date);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Update the cost basis for a users portfolio for dividends")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cost Basis updated"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found")
    })
    @PutMapping("/{userId}/cost_basis")
    public ResponseEntity<Void> updateCostBasisOnDividend(@PathVariable int userId, @RequestBody BigDecimal dividend) {

        //TODO BigDecimal costBasis = service.updateCostBasisOnDividend(userId, dividend).getPayload();
        return null;
    }

    @Operation(summary = "Update the account type for a portfolio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio Account type updated"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Portfolio> updateAccountType(@PathVariable int userId, @RequestParam AccountType accountType) {

        Result<Portfolio> result = service.updateAccountType(userId, accountType);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //TODO decide if we want this functionality
//    public ResponseEntity<Void> sellStockFromPortfolio(@PathVariable int userId, @PathVariable int stockId) {
//
//    }
//    public ResponseEntity<Portfolio> addStockToPortfolio(@PathVariable int userId, @RequestBody Stock stock) {
//
//    }
//    public ResponseEntity<Portfolio> deleteStockFromPortfolio(@PathVariable int userId, @PathVariable int stockId) {
//
//    }
}
