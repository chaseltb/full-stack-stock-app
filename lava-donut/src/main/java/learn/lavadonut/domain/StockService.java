package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StockService {
    private final StockRepository repository;

    public StockService(StockRepository repository) { this.repository = repository; }

    public List<Stock> findAll(){ return repository.findAll(); }

    public Result<List<Stock>> getStocksByIndustry(String industry) {
        Result<List<Stock>> result = new Result<>();

        if(Validations.isNullOrBlank(industry)){
            result.addMessage("industry must not be null or blank!", ResultType.INVALID);
            return result;
        }

        result.setPayload(repository.getStocksByIndustry(industry));

        return result;
    }

    public Stock findById(int stockId){ return repository.findById(stockId); }

    public Result<Stock> add(Stock stock){
        Result<Stock> result = validate(stock);

        if(!result.isSuccess()){
            return result;
        }

        if(stock.getId() != 0){
            result.addMessage("stock cannot be set for 'add' operation!", ResultType.INVALID);
            return result;
        }

        stock = repository.add(stock);
        result.setPayload(stock);
        return result;
    }

    public Result<Stock> update(Stock stock){
        Result<Stock> result = validate(stock);

        if(!result.isSuccess()){
            return result;
        }

        if(stock.getId() <= 0 ){
            result.addMessage("stock cannot be set for 'update' operation!", ResultType.INVALID);
            return result;
        }

        if(!repository.update(stock)){
            String error = String.format("stock: %s, could not be found!",
                    stock.getId());
            result.addMessage(error, ResultType.NOT_FOUND);
        }

        return result;
    }

    //TODO: may need to check if being used by other objects, come back to later
    public Result<Stock> delete(int stockId){
        Result<Stock> result = new Result<>();

        int usageCount = repository.getUsageCount(stockId);
        if(usageCount > 0){
            String error = String.format("stockId: %s, is in use", stockId);
            result.addMessage(error, ResultType.INVALID);
            return result;
        }

        if (!repository.deleteById(stockId)){
            String error = String.format("stock: %s, could not be found!",
                    stockId);
            result.addMessage(error, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<Stock> validate(Stock stock){
        Result<Stock> result = new Result<>();

        if(stock == null){
            result.addMessage("stock cannot be null!", ResultType.INVALID);
            return result;
        }

        if(Validations.isNullOrBlank(stock.getName())){
            result.addMessage("stock must have a name!", ResultType.INVALID);
        }
        if(Validations.isNullOrBlank(stock.getTicker())){
            result.addMessage("stock must have a ticker!", ResultType.INVALID);
        }

        if(stock.getCurrentPrice().compareTo(BigDecimal.ZERO) <= 0){
            result.addMessage("stock price must be greater than zero!", ResultType.INVALID);
        }

        if(stock.getStockExchange().getId() <= 0){
            result.addMessage("stock must have a listed Stock Exchange!", ResultType.INVALID);
        }

        if(stock.getCountry().getId() <= 0){
            result.addMessage("stock must have a listed Country!", ResultType.INVALID);
        }

        List<Stock> allStock = repository.findAll();

        for (Stock s : allStock){
            if(s.equals(stock)){
                result.addMessage("duplicate stocks are not allowed!", ResultType.INVALID);
            }
        }

        return result;
    }
}
