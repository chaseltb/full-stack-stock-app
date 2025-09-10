package learn.lavadonut.data;

import learn.lavadonut.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcRepositoryTest {

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

    }

    @Test
    void shouldFindByUsername() {
        User actual = repo.findByUsername("americanUser");
        assertNotNull(actual);
        assertEquals(1, actual.getUserId());
        assertEquals("americanUser", actual.getUsername());
        assertEquals("TEST FIRST NAME", actual.getFirstName());
        assertEquals("TEST LAST NAME", actual.getLastName());
        assertEquals(3, actual.getCurrencyId());

    }

    @Test
    void shouldNotFindNullUsername() {
        User actual = repo.findByUsername(null);
        assertNull(actual);
    }

    @Test
    void shouldAdd() {
        User user = getTestUser();
        User actual = repo.add(user);
        assertNotNull(actual);
        assertEquals(4, actual.getUserId());
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
        assertTrue(repo.deleteById(2));
    }

    @Test
    void shouldNotDeleteNonexistId() {
        assertTrue(repo.deleteById(9999));
    }

    private User getTestUser() {
        User user = new User();
        user.setCurrencyId(1);
        user.setFirstName("Jimmy");
        user.setLastName("Jam");
        user.setUsername("JimmyJam");
        return user;
    }
}