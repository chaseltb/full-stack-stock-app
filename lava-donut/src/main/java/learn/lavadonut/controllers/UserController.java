package learn.lavadonut.controllers;

import learn.lavadonut.domain.UserService;
import learn.lavadonut.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable int userId) {
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody User user) {
        return null;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable int userId) {
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable int userId) {
        return null;
    }
}
