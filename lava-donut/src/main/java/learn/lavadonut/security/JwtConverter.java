package learn.lavadonut.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    // 1. Signing key
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 2. "Configurable" constants
    private final String ISSUER = "field-agent";
    private final int EXPIRATION_MINUTES = 15;
    private final int EXPIRATION_MILLIS = EXPIRATION_MINUTES * 60 * 1000;

    // 3. Generate JWT token from user
    public String getTokenFromUser(User user) {
        // Convert authorities to a comma-separated string
        String authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Build the token with the necessary claims
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(user.getUsername())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    // 4. Extract user details from JWT token
    public User getUserFromToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        try {
            // Parse the JWT token
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));

            // Extract the username (subject) and authorities from the claims
            String username = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");

            // If authorities are missing or empty, assign a default authority
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (authStr != null && !authStr.isEmpty()) {
                authorities = Arrays.stream(authStr.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Default authority
            }

            // Return a User object with username and authorities
            return new User(username, "", authorities);

        } catch (JwtException e) {
            // Log or handle the JWT parsing error
            System.out.println("JWT Parsing Error: " + e.getMessage());
        }

        return null;
    }
}
