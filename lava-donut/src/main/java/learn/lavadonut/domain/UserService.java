package learn.lavadonut.domain;

import learn.lavadonut.data.UserRepository;
import learn.lavadonut.models.User;

import java.util.List;

public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> findAll() {
        return repo.findAll();
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Result<User> add(User user) {
        Result<User> result = validate(user);
        if (!result.isSuccess()) {
            return result;
        }

        if (user.getUserId() != 0) {
            result.addMessage("The user id can't be set for the add operation", ResultType.INVALID);
            return result;
        }

        if (repo.findByUsername(user.getUsername()) != null) {
            result.addMessage("There is already a user with this username", ResultType.INVALID);
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
        User existing = repo.findByUsername(user.getUsername());
        if (existing != null && existing.getUserId() != user.getUserId()) {
            result.addMessage("There is already a user with that username", ResultType.INVALID);
            return result;
        }

        if (!repo.update(user)) {
            String msg = String.format("Username: %s, was not found", user.getUsername());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

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

        // username
        if (Validations.isNullOrBlank(user.getUsername())) {
            result.addMessage("Username is required", ResultType.INVALID);
        } else if (user.getUsername().length() > 50) {
            result.addMessage("Username must be less than or equal to 50 characters", ResultType.INVALID);
        }

        // password
        if (Validations.isNullOrBlank(user.getPasswordHashed())) {
            result.addMessage("Password is required", ResultType.INVALID);
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

        // permission
        if (user.getPermission() == null) {
            result.addMessage("User permission level is required and must be a valid enum value", ResultType.INVALID);
        }

        // currency id
        if (user.getCurrencyId() <= 0) {
            result.addMessage("The user's currency id must be greater than 0", ResultType.INVALID);
        }

        return result;
    }
}
