package learn.lavadonut.data;

import learn.lavadonut.models.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findByUserId(int userId);

    User findByAppUserId(int userId);

    User add(User user);

    boolean update(User user);

    boolean deleteById(int userId);

    boolean doesCurrencyExist (int currencyId);

    boolean doesAppUserExist (int userAppId);
}
