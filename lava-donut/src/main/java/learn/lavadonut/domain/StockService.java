package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository repository;

    public StockService(StockRepository repository) { this.repository = repository; }
}
