package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.AssetType;
import learn.lavadonut.models.Stock;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
