package learn.lavadonut.controllers;

import learn.lavadonut.domain.OrderService;
import learn.lavadonut.models.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Spring DI and MVC
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/order") // Base URL
public class OrderController {
    private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable int id) {
        return null;
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Order>> findByUser(@PathVariable int userId) {
//        return null;
//    }

    @GetMapping("/{stockId}")
    public ResponseEntity<List<Order>> findByStock(@PathVariable int stockId) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Order order) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Order order) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        return null;
    }
}
