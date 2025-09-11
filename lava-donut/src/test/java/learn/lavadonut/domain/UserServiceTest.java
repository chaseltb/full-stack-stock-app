package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
import learn.lavadonut.data.UserRepository;
import learn.lavadonut.models.Currency;
import learn.lavadonut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepo;

    @MockBean
    CurrencyRepository currencyRepo;


    @BeforeEach
    public void setUp() {
        when(currencyRepo.findById(1))
                .thenReturn(getTestCurrency());
        when(userRepo.findByUserId(1)).thenReturn(null);
    }


    @Test
    public void shouldFindAll() {
        List<User> users = new ArrayList<>();
        User user1 = getTestUser();
        user1.setUserId(1);
        User user2 = getTestUser();
        user2.setUserId(2);
        user2.setAppUserId(2);
        users.add(user1);
        users.add(user2);
        when(userRepo.findAll()).thenReturn(users);
        List<User> result = service.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldFindByUserId() {
        User user = getTestUser();
        when(userRepo.findByUserId(user.getUserId())).thenReturn(user);
        User result = service.findByUserId(user.getUserId());
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void shouldNotFindNonexistingUsername() {
        User user = getTestUser();
        when(userRepo.findByUserId(user.getUserId())).thenReturn(null);
        User result = service.findByUserId(user.getUserId());
        assertNull(result);
    }

    @Test
    public void shouldAddUser() {
        User user = getTestUser();
        User expected = getTestUser();
        expected.setUserId(1);
        when(userRepo.add(user)).thenReturn(expected);
        when(userRepo.doesCurrencyExist(user.getCurrencyId())).thenReturn(true);
        when(userRepo.doesAppUserExist(user.getAppUserId())).thenReturn(true);
        Result<User> result = service.add(user);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), expected);

    }

    @Test
    public void shouldNotAddUserWithId() {
        User user = getTestUser();
        user.setUserId(1);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserWithNegativeCurrencyId() {
        User user = getTestUser();
        user.setCurrencyId(-1);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserWithNonExistingCurrencyId() {
        User user = getTestUser();
        user.setCurrencyId(999);
        when(currencyRepo.findById(999))
                .thenReturn(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserNullFirstName() {
        User user = getTestUser();
        user.setFirstName(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserBlankFirstName() {
        User user = getTestUser();
        user.setFirstName("");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddOver50CharFirstName() {
        User user = getTestUser();
        user.setFirstName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }


    @Test
    public void shouldNotAddUserNullLastName() {
        User user = getTestUser();
        user.setLastName(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserBlankLastName() {
        User user = getTestUser();
        user.setLastName("");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddOver50CharLastName() {
        User user = getTestUser();
        user.setLastName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserNoAppUserId() {
        User user = getTestUser();
        user.setAppUserId(0);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserWithDuplicateAppUserId() {
        User user = getTestUser();
        user.setAppUserId(1);

        User existing = getTestUser();
        existing.setUserId(10);
        existing.setAppUserId(1);

        when(userRepo.findByUserId(1)).thenReturn(existing);

        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldUpdateUser() {
        User user = getTestUpdateUser();
        user.setAppUserId(2);
        user.setFirstName("James");

        when(userRepo.update(user)).thenReturn(true);
        when(userRepo.doesCurrencyExist(user.getCurrencyId())).thenReturn(true);
        when(userRepo.doesAppUserExist(user.getAppUserId())).thenReturn(true);
        Result<User> result = service.update(user);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), user);

    }
    @Test
    public void shouldNotUpdateUserWithoutId() {
        User user = getTestUpdateUser();
        user.setUserId(0);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithChangedId() {
        User user = getTestUpdateUser();
        user.setUserId(10);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithNegativeCurrencyId() {
        User user = getTestUpdateUser();
        user.setCurrencyId(-1);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithNonExistingCurrencyId() {
        User user = getTestUpdateUser();
        user.setCurrencyId(999);
        when(currencyRepo.findById(999))
                .thenReturn(null);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserNullFirstName() {
        User user = getTestUpdateUser();
        user.setFirstName(null);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserBlankFirstName() {
        User user = getTestUpdateUser();
        user.setFirstName("");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateOver50CharFirstName() {
        User user = getTestUpdateUser();
        user.setFirstName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }


    @Test
    public void shouldNotUpdateUserNullLastName() {
        User user = getTestUpdateUser();
        user.setLastName(null);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserBlankLastName() {
        User user = getTestUpdateUser();
        user.setLastName("");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateOver50CharLastName() {
        User user = getTestUpdateUser();
        user.setLastName("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserNoAppUserId() {
        User user = getTestUser();
        user.setAppUserId(0);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithDuplicateAppUserId() {
        User user = getTestUpdateUser();
        user.setAppUserId(2);

        User existing = getTestUser();
        existing.setUserId(99);
        existing.setAppUserId(2);

        when(userRepo.findByUserId(2)).thenReturn(existing);

        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldDeleteUser() {
        when(userRepo.deleteById(1)).thenReturn(true);
        boolean result = service.deleteById(1);
        assertTrue(result);

    }

    @Test
    public void shouldNotDeleteNonexistingUser() {
        when(userRepo.deleteById(999)).thenReturn(false);
        boolean result = service.deleteById(999);
        assertFalse(result);
    }

    @Test
    public void shouldNotDeleteNegativeUser() {
        when(userRepo.deleteById(-1)).thenReturn(false);
        boolean result = service.deleteById(-1);
        assertFalse(result);
    }

    private User getTestUser() {
        User user = new User();
        user.setCurrencyId(1);
        user.setFirstName("Jimmy");
        user.setLastName("Jam");
        user.setAppUserId(1);
        return user;
    }

    private User getTestUpdateUser() {
        User user = getTestUser();
        user.setUserId(1);
        return user;
    }

    private Currency getTestCurrency() {
        Currency c = new Currency();
        c.setId(1);
        c.setCode("TST");
        c.setName("Test Testudo");
        c.setValueToUsd(new BigDecimal(999));
        return c;
    }

}