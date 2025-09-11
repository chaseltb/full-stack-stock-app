package learn.lavadonut.domain;

import learn.lavadonut.data.StockExchangeRepository;
import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.Stock;
import learn.lavadonut.models.StockExchange;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TimeZone;

@Service
public class StockExchangeService {

    private final StockExchangeRepository repo;
    private final StockRepository stockRepo;

    public StockExchangeService(StockExchangeRepository repo, StockRepository stockRepo) {
        this.repo = repo;
        this.stockRepo = stockRepo;
    }

    public List<StockExchange> findAll() {
        return repo.findAll();
    }

    public StockExchange findById(int id) {
        return repo.findById(id);
    }

    public Result<StockExchange> add(StockExchange stockExchange) {
        Result<StockExchange> result = validate(stockExchange);
        if (!result.isSuccess()) {
            return result;
        }

        if (stockExchange.getId() != 0) {
            result.addMessage("The stock exchange id can't be set for the add operation", ResultType.INVALID);
            return result;
        }

        StockExchange finalStockExchange = stockExchange;
        StockExchange existing = repo.findAll()
                .stream()
                .filter(s -> s.getCode().equalsIgnoreCase(finalStockExchange.getCode()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            result.addMessage("There is already a stock exchange with that code", ResultType.INVALID);
            return result;
        }

        stockExchange = repo.add(stockExchange);
        result.setPayload(stockExchange);
        return result;
    }

    public Result<StockExchange> update(StockExchange stockExchange) {
        Result<StockExchange> result = validate(stockExchange);
        if (!result.isSuccess()) {
            return result;
        }

        if (stockExchange.getId() <= 0) {
            result.addMessage("The stock exchange id must be set for the update operation", ResultType.INVALID);
            return result;
        }

        // duplicate check
        StockExchange existing = repo.findAll()
                .stream()
                .filter(s -> s.getCode().equalsIgnoreCase(stockExchange.getCode()))
                .findFirst()
                .orElse(null);

        if (existing != null && existing.getId() != stockExchange.getId()) {
            result.addMessage("There is already a stock exchange with that code", ResultType.INVALID);
            return result;
        }

        if (!repo.update(stockExchange)) {
            String msg = String.format("Stock exchange id: %s, was not found", stockExchange.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<Void> deleteById(int exchangeId) {
        Result<Void> result = new Result<>();
        List<Stock> stocks = stockRepo.findAll();
        if (stocks != null && stocks.stream().anyMatch(s -> s.getStockExchange().getId() == exchangeId)) {
            result.addMessage("Cannot delete exchange with existing stocks", ResultType.INVALID);
            return result;
        }
        if (!repo.deleteById(exchangeId)) {
            result.addMessage("Exchange not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<StockExchange> validate(StockExchange stockExchange) {
        Result<StockExchange> result = new Result<>();
        if (stockExchange == null) {
            result.addMessage("Stock Exchange can't be null", ResultType.INVALID);
            return result;
        }

        // name
        if (Validations.isNullOrBlank(stockExchange.getName())) {
            result.addMessage("Name is required", ResultType.INVALID);
        } else if (stockExchange.getName().length() > 100) {
            result.addMessage("Name must be less than or equal to 100 characters", ResultType.INVALID);
        }

        // code
        if (Validations.isNullOrBlank(stockExchange.getCode())) {
            result.addMessage("Code is required", ResultType.INVALID);
        } else if (stockExchange.getCode().length() > 10) {
            result.addMessage("Code must be less than or equal to 10 characters", ResultType.INVALID);
        } else if (!stockExchange.getCode().matches("^[A-Z0-9]+$")) {
            result.addMessage("Code must contain only uppercase letters and numbers", ResultType.INVALID);
        }

        // timeZone
        if (Validations.isNullOrBlank(stockExchange.getTimeZone())) {
            result.addMessage("Time Zone is required", ResultType.INVALID);
        } else {
            try {
                TimeZone.getTimeZone(stockExchange.getTimeZone());
                if (!java.time.ZoneId.getAvailableZoneIds().contains(stockExchange.getTimeZone())) {
                    result.addMessage("Time Zone must be a valid timezone identifier", ResultType.INVALID);
                }
            } catch (Exception e) {
                result.addMessage("Time Zone must be a valid timezone identifier", ResultType.INVALID);
            }
        }

        return result;
    }
}