package learn.lavadonut.domain;

import learn.lavadonut.data.CountryRepository;
import learn.lavadonut.models.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryRepository repository;

    public CountryService(CountryRepository repository) {
        this.repository = repository;
    }

    public List<Country> findAll() {
        return repository.findAll();
    }

    public Country findById(int id) {
        return repository.findById(id);
    }

    public Result<Country> add(Country country) {
        Result<Country> result = validate(country);
        if (!result.isSuccess()) {
            return result;
        }
        country = repository.add(country);
        result.setPayload(country);
        return result;
    }

    public Result<Country> update(Country country) {
        Result<Country> result = validate(country);
        if (!result.isSuccess()) {
            return result;
        }

        if (country.getId() <= 0) {
            result.addMessage("country id must be set to update an country", ResultType.INVALID);
            return result;
        }

        if (!repository.update(country)) {
            String msg = String.format("order id: %s, not found", country.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        } else {
            result.setPayload(country);
        }

        return result;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private Result<Country> validate(Country country) {
        Result<Country> result = new Result<>();
        if (country == null) {
            result.addMessage("country is required", ResultType.INVALID);
            return result;
        }

        if (country.getCurrency() == null) {
            result.addMessage("currency is required", ResultType.INVALID);
        }

        if (isNullOrBlank(country.getName())) {
            result.addMessage("name is required and cannot be blank", ResultType.INVALID);
        }

        if (isNullOrBlank(country.getCode())) {
            result.addMessage("code is required and cannot be blank", ResultType.INVALID);
        }

        boolean uniqueCode = findAll().stream()
                .noneMatch(c -> c.getCode().equalsIgnoreCase(country.getCode()));
        if (!uniqueCode) {
            result.addMessage("code must be unique", ResultType.INVALID);
        }

        return result;
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
