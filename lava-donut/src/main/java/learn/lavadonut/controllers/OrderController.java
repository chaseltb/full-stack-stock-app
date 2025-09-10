package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order API", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @Operation(summary = "Find all orders")
    @ApiResponse(responseCode = "200", description = "Orders found")
    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        List<Order> orders = service.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK); // 200
    }

    @Operation(summary = "Find an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable int id) {
        Order order = service.findById(id);
        if (order == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

//    @Operation(summary = "Find orders by user")
//    @ApiResponse(responseCode = "200", description = "Orders found")
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Order>> findByUser(@PathVariable int userId) {
//        List<Order> orders = service.findByUser(userId);
//        return new ResponseEntity<>(orders, HttpStatus.OK);
//    }

    @Operation(summary = "Find orders by stock")
    @ApiResponse(responseCode = "200", description = "Orders found")
    @GetMapping("/{stockId}")
    public ResponseEntity<List<Order>> findByStock(@PathVariable int stockId) {
        List<Order> orders = service.findByStock(stockId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Add an order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created"),
            @ApiResponse(responseCode = "400", description = "Invalid order")
    })
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Order order) {
        Result<Order> result = service.add(order);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
        return ErrorResponse.build(result);
    }

    @Operation(summary = "Update an order")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order updated"),
            @ApiResponse(responseCode = "400", description = "Invalid order"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
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

    @Operation(summary = "Delete an order")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
    }
}
