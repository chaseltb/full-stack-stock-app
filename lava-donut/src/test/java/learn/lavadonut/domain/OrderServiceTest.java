package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrderServiceTest {
    @Autowired
    OrderService service;

    @MockBean
    OrderRepository repository;

    @Test
    void shouldFindAll() {
        List<Order> orders = service.findAll();
        assertNotNull(orders);

        // can't predict order
        // if delete is first, we're down to 1
        // if add is first, we may go as high as 7
        assertTrue(orders.size() <= 7);
    }

    @Test
    void shouldFindById() {
        Order order = makeOrder();
        when(repository.findById(1)).thenReturn(order);
        Order actual = service.findById(1);
        assertEquals(order, actual);
    }

//    @Test
//    void shouldFindByUser() {
//        List<Order> orders = List.of(makeOrder());
//
//        List<Order> actual = service.findByUser(1);
//        assertEquals(orders, actual);
//
//        actual = service.findByUser(999);
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
        when(repository.findByStock(7)).thenReturn(orders);
        List<Order> actual = service.findByStock(7);
        assertEquals(orders, actual);

        actual = service.findByStock(999);
        assertEquals(new ArrayList<Order>(), actual);
    }

    // add
    @Test
    void shouldAdd() {
        Order expected = makeOrder();
        Order arg = makeOrder();
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<Order> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddNullOrder() {
        Result<Order> result = service.add(null);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithNullTransactionType() {
        Order order = makeOrder();
        order.setTransactionType(null);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithInvalidStockId() {
        Order order = makeOrder();
        order.setStockId(-1);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithNullShares() {
        Order order = makeOrder();
        order.setNumberOfShares(null);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithInvalidShares() {
        Order order = makeOrder();
        order.setNumberOfShares(new BigDecimal("-1.0"));
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithNullDate() {
        Order order = makeOrder();
        order.setDate(null);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithNullPrice() {
        Order order = makeOrder();
        order.setPrice(null);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithInvalidPrice() {
        Order order = makeOrder();
        order.setPrice(BigDecimal.valueOf(-10.0));
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    // update
    @Test
    void shouldUpdate() {
        Order order = makeOrder();
        when(repository.update(order)).thenReturn(true);
        Result<Order> result = service.update(order);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertNotNull(result.getPayload());
        assertEquals(order, result.getPayload());
    }

    @Test
    void shouldNotUpdateMissingOrder() {
        Order order = makeOrder();
        order.setId(999);
        when(repository.update(order)).thenReturn(false);
        Result<Order> result = service.update(order);

        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldDelete() {
        when(repository.delete(2)).thenReturn(true);
        assertTrue(service.delete(2));

        when(repository.delete(2)).thenReturn(false);
        assertFalse(service.delete(2));
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