package learn.lavadonut.controllers;

import learn.lavadonut.domain.PortfolioService;
import learn.lavadonut.models.AccountType;
import learn.lavadonut.models.Portfolio;
import learn.lavadonut.models.Stock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioController {

    private final PortfolioService service;

    public PortfolioController(PortfolioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Portfolio> findByUserId(@PathVariable int userId) {

        return null;
    }

    @GetMapping
    public ResponseEntity<List<Stock>> findAllStocksInPortfolio(@PathVariable int userId) {

        return null;
    }
    @GetMapping
    public ResponseEntity<BigDecimal> getPortfolioValue(@PathVariable int userId, @RequestParam String date) {

        return null;
    }
    @GetMapping
    public ResponseEntity<Void> getCostBasisOnDividend(@PathVariable int userId, @RequestBody BigDecimal dividend) {

        return null;
    }
    @PutMapping
    public ResponseEntity<Portfolio> updateAccountType(@PathVariable int userId, @RequestParam AccountType accountType) {

        return null;
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
