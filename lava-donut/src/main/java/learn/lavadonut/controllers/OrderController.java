package learn.lavadonut.controllers;

import learn.lavadonut.domain.OrderService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Spring DI and MVC
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/order") // Base URL
public class OrderController {
    private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }
}
