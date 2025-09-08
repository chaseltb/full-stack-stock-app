package learn.lavadonut.data;

import learn.lavadonut.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderJdbcTemplateRepositoryTest {
    @Autowired
    OrderJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Order> orders = repository.findAll();
        assertNotNull(orders);

        // can't predict order
        // if delete is first, we're down to 1
        // if add is first, we may go as high as 3
        assertTrue(orders.size() >= 1 && orders.size() <= 3);
    }

    @Test
    void shouldFindById() {
        Order order = makeOrder();

        Order actual = repository.findById(1);
        assertEquals(order, actual);

        actual = repository.findById(999);
        assertNull((actual));
    }

//    @Test
//    void shouldFindByUser() {
//        List<Order> orders = List.of(makeOrder());
//
//        List<Order> actual = repository.findByUser(1);
//        assertEquals(orders, actual);
//
//        actual = repository.findByUser(999);
//        assertNull((actual));
//    }

    @Test
    void shouldFindByStock() {
        List<Order> orders = List.of(makeOrder());

        List<Order> actual = repository.findByStock(1);
        assertEquals(orders, actual);

        actual = repository.findByStock(999);
        assertNull((actual));
    }

    @Test
    void shouldAdd() {
        Order order = makeOrder();
        Order actual = repository.add(order);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdate() {
        Order order = makeOrder();
        order.setNumberOfShares(15.0);
        assertTrue(repository.update(order));
        assertFalse(repository.update(new Order()));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(1));
        assertFalse(repository.delete(1));
    }

    Order makeOrder() {
        return new Order(
                1,
                TransactionType.BUY,
                1,
                12.0,
                ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2022, 12, 12), LocalTime.MIDNIGHT), ZoneId.of("UTC")),
                BigDecimal.TEN,
                1);
    }
}