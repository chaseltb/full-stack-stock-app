package learn.lavadonut.data;

import learn.lavadonut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserJdbcRepositoryTest {

    private final int NEXT_ID = 5;

    @Autowired
    UserJdbcRepository repo;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<User> users = repo.findAll();
        assertNotNull(users);
        assertTrue(users.size() >= 4);
    }

    @Test
    void shouldFindByUserId() {
        User actual = repo.findByUserId(1);
        assertNotNull(actual);
        assertEquals(1, actual.getUserId());
        assertEquals("TEST FIRST NAME", actual.getFirstName());
        assertEquals("TEST LAST NAME", actual.getLastName());
        assertEquals(1, actual.getAppUserId());
        assertEquals(1, actual.getCurrencyId());

    }

    @Test
    void shouldNotFindZeroUserId() {
        User actual = repo.findByUserId(0);
        assertNull(actual);
    }

    @Test
    void shouldAdd() {
        User user = getTestUser();
        User actual = repo.add(user);
        assertNotNull(actual);
        assertTrue(NEXT_ID == actual.getUserId() || NEXT_ID - 1 == actual.getUserId());
    }

    @Test
    void shouldUpdate() {
        User user = getTestUser();
        user.setUserId(3);
        assertTrue(repo.update(user));
    }

    @Test
    void shouldNotUpdateNonExistingId() {
        User user = getTestUser();
        user.setUserId(9999);
        assertFalse(repo.update(user));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repo.deleteById(4)); // should link to test for delete entry in test db
    }

    @Test
    void shouldNotDeleteNonexistId() {
        assertFalse(repo.deleteById(9999));
    }

    private User getTestUser() {
        User user = new User();
        user.setCurrencyId(1);
        user.setFirstName("Jimmy");
        user.setLastName("Jam");
        user.setAppUserId(1);
        return user;
    }
}