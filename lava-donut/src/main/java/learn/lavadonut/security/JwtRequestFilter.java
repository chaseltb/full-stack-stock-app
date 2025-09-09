package learn.lavadonut.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1: Get Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Step 2: Check for Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Step 3: Validate token
            if (jwtUtil.isTokenValid(token)) {
                // Step 4: Extract username and role from claims
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                // Step 5: Create authorities list
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                // Step 6: Create authenticated token
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // Step 7: Store in security context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
