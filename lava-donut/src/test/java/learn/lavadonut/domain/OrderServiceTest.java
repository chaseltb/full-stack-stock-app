package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.models.Order;
import learn.lavadonut.models.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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