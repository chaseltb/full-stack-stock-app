package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.lavadonut.domain.PortfolioService;
import learn.lavadonut.domain.Result;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Portfolio>> findPortfoliosByUserId(@PathVariable int userId) {
        List<Portfolio> portfolios = service.findPortfoliosByUserId(userId);
        if(portfolios == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(portfolios);
    }

    @Operation(summary = "Find all stocks in a users portfolio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Portfolio Stocks"),
            @ApiResponse(responseCode = "404", description = "Portfolio Stocks not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{portfolioId}/stocks")
    public ResponseEntity<List<Stock>> findAllStocksInPortfolio(@PathVariable int portfolioId) {
        List<Stock> stocks = service.findAllOwnedStocksInPortfolio(portfolioId);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{portfolioId}/value")
    public ResponseEntity<BigDecimal> getPortfolioValue(@PathVariable int portfolioId, @RequestParam String date) {
        Result<BigDecimal> result = service.getPortfolioValue(portfolioId, date);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{portfolioId}")
    public ResponseEntity<Portfolio> updateAccountType(@PathVariable int portfolioId, @RequestParam Portfolio portfolio) {

        if (portfolioId != portfolio.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<Portfolio> result = service.updateAccountType(portfolio);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Operation(summary = "Add an order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid order"),
            @ApiResponse(responseCode = "404", description = "Portfolio not found")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/{portfolioId}/order")
    public ResponseEntity<Object> addOrderFromPortfolio(@PathVariable int portfolioId,@RequestBody Order order) {
        Result<Portfolio> result = service.addOrderToPortfolio(portfolioId, order);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
        return ErrorResponse.build(result);
    }

}
