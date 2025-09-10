package learn.lavadonut.controllers;

import learn.lavadonut.domain.OrderService;
import learn.lavadonut.domain.Result;
import learn.lavadonut.models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring DI and MVC
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/order") // Base URL
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = service.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK); // 200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable int id) {
        Order order = service.findById(id);
        if (order == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Order>> findByUser(@PathVariable int userId) {
//        return null;
//    }

    @GetMapping("/{stockId}")
    public ResponseEntity<List<Order>> findByStock(@PathVariable int stockId) {
        List<Order> orders = service.findByStock(stockId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Order order) {
        Result<Order> result = service.add(order);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Order order) {
        // validate id
        if (id != order.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409
        }

        Result<Order> result = service.update(order);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
    }
}
