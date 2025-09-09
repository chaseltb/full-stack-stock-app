package learn.lavadonut.controllers;

import learn.lavadonut.security.AuthRequest;
import learn.lavadonut.security.AuthResponse;
import learn.lavadonut.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        // Hardcoded username/password check (for now)
        if ("user".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
            String token = jwtUtil.generateToken(authRequest.getUsername(), "ROLE_USER");
            return ResponseEntity.ok(new AuthResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
