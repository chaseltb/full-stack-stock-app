package learn.lavadonut.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import learn.lavadonut.domain.Result;
import learn.lavadonut.domain.UserService;
import learn.lavadonut.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"})
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> findAll() {
        return service.findAll();
    }

    @Operation(summary = "find user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<Object> findById(@PathVariable int userId) {
        User user = service.findByUserId(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Add a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid user input")
    })
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody User user) {
        Result<User> result = service.add(user);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "Update a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "ID conflict")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable int userId, @RequestBody User user) {
        if (userId != user.getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<User> result = service.update(user);

        if (!result.isSuccess()) {
            return ErrorResponse.build(result);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable int userId) {
        if (service.deleteById(userId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
