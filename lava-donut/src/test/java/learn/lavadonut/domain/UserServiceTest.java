package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
import learn.lavadonut.data.UserRepository;
import learn.lavadonut.models.Currency;
import learn.lavadonut.models.User;
import learn.lavadonut.models.UserType;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
        when(userRepo.findByUsername("JimmyJam")).thenReturn(null);
    }


    @Test
    public void shouldFindAll() {
        List<User> users = new ArrayList<>();
        User user1 = getTestUser();
        user1.setUserId(1);
        User user2 = getTestUser();
        user2.setUserId(2);
        user2.setUsername("JJJ");
        users.add(user1);
        users.add(user2);
        when(userRepo.findAll()).thenReturn(users);
        List<User> result = service.findAll();
        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldFindByUsername() {
        User user = getTestUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        User result = service.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void shouldNotFindNonexistingUsername() {
        User user = getTestUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(null);
        User result = service.findByUsername(user.getUsername());
        assertNull(result);
    }

    @Test
    public void shouldAddUser() {
        User user = getTestUser();
        User expected = getTestUser();
        expected.setUserId(1);
        when(userRepo.add(user)).thenReturn(expected);
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
    public void shouldNotAddUserWithNullUsername() {
        User user = getTestUser();
        user.setUsername(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserWithBlankUsername() {
        User user = getTestUser();
        user.setUsername("");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddRepeatUsername() {
        User user = getTestUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddOver50CharUsername() {
        User user = getTestUser();
        user.setUsername("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserNullPassword() {
        User user = getTestUser();
        user.setPasswordHashed(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserBlankPassword() {
        User user = getTestUser();
        user.setPasswordHashed("");
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotAddUserWithPositiveCurrencyId() {
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
    public void shouldNotAddUserWithNullPermission() {

        User user = getTestUser();
        user.setPermission(null);
        Result<User> result = service.add(user);
        assertFalse(result.isSuccess());

    }


    @Test
    public void shouldUpdateUser() {
        User user = getTestUpdateUser();
        user.setPermission(UserType.ADMIN);
        user.setUsername("Jimbo");
        user.setPasswordHashed("aef4315123");
        user.setFirstName("James");

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
    public void shouldNotUpdateUserWithNullUsername() {
        User user = getTestUpdateUser();
        user.setUsername(null);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithBlankUsername() {
        User user = getTestUpdateUser();
        user.setUsername("");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateRepeatUsername() {
        User user = getTestUpdateUser();
        User repeat = getTestUpdateUser();
        repeat.setUserId(10);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(repeat);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateOver50CharUsername() {
        User user = getTestUpdateUser();
        user.setUsername("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserNullPassword() {
        User user = getTestUpdateUser();
        user.setPasswordHashed(null);
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserBlankPassword() {
        User user = getTestUpdateUser();
        user.setPasswordHashed("");
        Result<User> result = service.update(user);
        assertFalse(result.isSuccess());
    }

    @Test
    public void shouldNotUpdateUserWithPositiveCurrencyId() {
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
    public void shouldNotUpdateUserWithNullPermission() {

        User user = getTestUpdateUser();
        user.setPermission(null);
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
        user.setUsername("JimmyJam");
        user.setPasswordHashed("D35AE22394");
        user.setPermission(UserType.USER);
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