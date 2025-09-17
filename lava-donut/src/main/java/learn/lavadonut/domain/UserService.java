package learn.lavadonut.domain;

import learn.lavadonut.data.AppUserRepository;
import learn.lavadonut.data.CurrencyRepository;
import learn.lavadonut.data.UserRepository;
import learn.lavadonut.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo, CurrencyRepository currencyRepository, AppUserRepository appUserRepository) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findByUserId(int userId) {
        return repo.findByUserId(userId);
    }

    public User findByAppUserId(int appUserId) { return repo.findByAppUserId(appUserId);}

    public Result<User> add(User user) {
        Result<User> result = validate(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getUserId() != 0) {
            result.addMessage("The user id can't be set for the add operation", ResultType.INVALID);
            return result;
        }

        if (repo.findByUserId(user.getUserId()) != null) {
            result.addMessage("There is already a user with this user id", ResultType.INVALID);
            return result;
        }

        user = repo.add(user);
        result.setPayload(user);
        return result;
    }


    public Result<User> update(User user) {
        Result<User> result = validate(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getUserId() <= 0) {
            result.addMessage("The user id must be set for the update operation", ResultType.INVALID);
            return result;
        }

        // duplicate check
        User existing = repo.findByUserId(user.getUserId());
        if (existing != null) {
            result.addMessage("There is already a user with that user id", ResultType.INVALID);
            return result;
        }

        if (!repo.update(user)) {
            String msg = String.format("User Id: %s, was not found", user.getUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        result.setPayload(user);
        return result;
    }

    public boolean deleteById(int userId) {
        return repo.deleteById(userId);
    }

    private Result<User> validate(User user) {
        Result<User> result = new Result<>();
        if (user == null) {
            result.addMessage("User can't be null", ResultType.INVALID);
            return result;
        }

        // first name
        if (Validations.isNullOrBlank(user.getFirstName())) {
            result.addMessage("First Name is required", ResultType.INVALID);
        } else if (user.getFirstName().length() > 50) {
            result.addMessage("First Name must be less than or equal to 50 characters", ResultType.INVALID);
        }

        // last name
        if (Validations.isNullOrBlank(user.getLastName())) {
            result.addMessage("Last Name is required", ResultType.INVALID);
        } else if (user.getLastName().length() > 50) {
            result.addMessage("Last Name must be less than or equal to 50 characters", ResultType.INVALID);
        }

        // currency id
        if (user.getCurrencyId() <= 0) {
            result.addMessage("The user's currency id must be greater than 0", ResultType.INVALID);
        }

        if (!repo.doesCurrencyExist(user.getCurrencyId())){
            result.addMessage("The user's currency id must be that of an existing currency", ResultType.INVALID);
        }

        // app user id
        if (user.getAppUserId() <= 0) {
            result.addMessage("The user's app user id must be greater than 0", ResultType.INVALID);
        }

        if (!repo.doesAppUserExist(user.getAppUserId())){
            result.addMessage("The user's appUser id must be that of an existing appUser", ResultType.INVALID);
        }

        return result;
    }

}
