package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.models.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.*;
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
        // if add is first, we may go as high as 3
        assertTrue(orders.size() >= 1 && orders.size() <= 3);
    }

    @Test
    void shouldFindById() {
        Order order = makeOrder();

        Order actual = service.findById(1);
        assertEquals(order, actual);

        actual = service.findById(999);
        assertNull((actual));
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
        List<Order> orders = List.of(makeOrder());

        List<Order> actual = service.findByStock(1);
        assertEquals(orders, actual);

        actual = service.findByStock(999);
        assertNull((actual));
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
        Order order = makeOrder();
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
    void shouldNotAddOrderWithInvalidShares() {
        Order order = makeOrder();
        order.setNumberOfShares(-1.0);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithNullDate() {
        Order order = makeOrder();
        order.setDateTime(null);
        Result<Order> result = service.add(order);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddOrderWithFutureDate() {
        Order order = makeOrder();
        order.setDateTime(ZonedDateTime.now().plusDays(1));
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
        when(repository.update(order)).thenReturn(false);
        Result<Order> result = service.update(order);

        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertNull(result.getPayload());
    }

    @Test
    void shouldDelete() {
        assertTrue(service.delete(1));
        assertFalse(service.delete(1));
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