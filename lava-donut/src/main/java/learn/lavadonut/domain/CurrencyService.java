package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) { this.repository = repository; }

    
}
