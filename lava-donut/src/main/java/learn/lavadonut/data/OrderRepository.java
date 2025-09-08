package learn.lavadonut.data;

import learn.lavadonut.models.Order;

import java.util.List;

public interface OrderRepository {
    Order add(Order order);

    Order findById(int id);

    List<Order> findAll();

//    List<Order> findByUser(int userId);

    List<Order> findByStock(int stockId);

    boolean update(Order order);

    boolean delete(int id);
}
