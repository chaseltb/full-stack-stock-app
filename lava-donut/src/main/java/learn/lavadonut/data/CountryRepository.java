package learn.lavadonut.data;

import learn.lavadonut.models.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> findAll();

    Country findById(int id);

    Country add(Country country);

    boolean update(Country country);

    boolean delete(int id);
}
