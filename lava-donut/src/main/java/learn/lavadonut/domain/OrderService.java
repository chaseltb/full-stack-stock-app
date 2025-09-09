package learn.lavadonut.domain;

import learn.lavadonut.data.OrderRepository;
import learn.lavadonut.models.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrderService {
    // fields
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Result<Order> add(Order order) {
        Result<Order> result = validate(order);
        if (!result.isSuccess()) {
            return result;
        }
        order = repository.add(order);
        result.setPayload(order);
        return result;
    }

    public Order findById(int id) {
        return repository.findById(id);
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

//    public List<Order> findByUser(int userId) {
//      return repository.findByUser(userId);
//    }

    public List<Order> findByStock(int stockId) {
        return repository.findByStock(stockId);
    }

    public Result<Order> update(Order order) {
        Result<Order> result = validate(order);
        if (!result.isSuccess()) {
            return result;
        }

        if (order.getId() <= 0) {
            result.addMessage("order id must be set to update an order", ResultType.INVALID);
            return result;
        }

        if (!repository.update(order)) {
            String msg = String.format("order id: %s, not found", order.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        result.setPayload(order);
        return result;
    }

    public boolean delete(int id) {
        return repository.delete(id);
    }

    private Result<Order> validate(Order order) {
        Result<Order> result = new Result<>();
        if (order == null) {
            result.addMessage("order cannot be null", ResultType.INVALID);
            return result;
        }

        // transaction type is required
        if (order.getTransactionType() == null) {
            result.addMessage("transaction type is required", ResultType.INVALID);
        }

        // stock id is required
        if (order.getStockId() <= 0) {
            result.addMessage("stock id is required", ResultType.INVALID);
        }

        // shares must be greater than 0
        if (order.getNumberOfShares() <= 0) {
            result.addMessage("shares must be greater than 0", ResultType.INVALID);
        }

        // date is required
        if (order.getDateTime() == null) {
            result.addMessage("date is required", ResultType.INVALID);
        }

        // date cannot be in the future
        if (order.getDateTime().isAfter(ZonedDateTime.now())) {
            result.addMessage("date cannot be in the future", ResultType.INVALID);
        }

        // price is required
        if (order.getPrice() == null) {
            result.addMessage("price is required", ResultType.INVALID);
        }

        // price must be greater than 0
        if (order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            result.addMessage("price must be greater than 0", ResultType.INVALID);
        }

        return result;
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
