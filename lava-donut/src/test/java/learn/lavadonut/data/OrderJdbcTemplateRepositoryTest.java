package learn.lavadonut.data;

import learn.lavadonut.models.Order;
import learn.lavadonut.models.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        // if add is first, we may go as high as 7
        assertTrue(!orders.isEmpty() && orders.size() <= 7);
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
        Order order = new Order(
                5,
                TransactionType.BUY,
                7,
                new BigDecimal("22"),
                Date.valueOf(LocalDate.of(2015, 10, 1)),
                new BigDecimal("3.75")
        );
        List<Order> orders = List.of(order);

        List<Order> actual = repository.findByStock(7);
        assertEquals(orders, actual);

        actual = repository.findByStock(999);
        assertEquals(new ArrayList<Order>(), actual);
    }

    @Test
    void shouldAdd() {
        Order order = makeOrder();
        Order actual = repository.add(order);
        assertNotNull(actual);
    }

    @Test
    void shouldUpdate() {
        Order order = new Order(
                3,
                TransactionType.BUY,
                1,
                new BigDecimal("5.075"),
                Date.valueOf(LocalDate.of(2022, 5, 17)),
                new BigDecimal("12.915")
        );
        System.out.println(order);
        System.out.println(repository.findById(3));
        assertTrue(repository.update(order));


        order.setId(999);
        assertFalse(repository.update(order));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.delete(2));
        assertFalse(repository.delete(2));
    }

    Order makeOrder() {
        return new Order(
                1,
                TransactionType.BUY,
                1,
                new BigDecimal("20.0"),
                Date.valueOf(LocalDate.of(2025, 5, 17)),
                new BigDecimal("12.915")
                );
    }
}