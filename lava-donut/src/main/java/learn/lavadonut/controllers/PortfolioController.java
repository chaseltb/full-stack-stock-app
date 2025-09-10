package learn.lavadonut.controllers;

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
public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Portfolio> findByUserId(@PathVariable int userId) {
        Portfolio portfolio = service.findByUserId(userId);
        if(portfolio == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/{userId}/stocks")
    public ResponseEntity<List<Stock>> findAllStocksInPortfolio(@PathVariable int userId) {
        List<Stock> stocks = service.findAllOwnedStocksInPortfolio(userId);
        if (stocks == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(stocks);
    }
    @GetMapping("/{userId}/value")
    public ResponseEntity<BigDecimal> getPortfolioValue(@PathVariable int userId, @RequestParam String date) {
        Result<BigDecimal> result = service.getPortfolioValue(userId, date);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getPayload());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/userId/cost_basis")
    public ResponseEntity<Void> getCostBasisOnDividend(@PathVariable int userId, @RequestBody BigDecimal dividend) {

        //TODO BigDecimal costBasis = service.updateCostBasisOnDividend(userId, dividend).getPayload();
        return null;
    }
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
