package learn.lavadonut.data;

import learn.lavadonut.domain.ResultType;
import learn.lavadonut.models.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> findAll();

    Country findById(int id);

    Country add(Country country);

    boolean update(Country country);

    ResultType delete(int id);
}
