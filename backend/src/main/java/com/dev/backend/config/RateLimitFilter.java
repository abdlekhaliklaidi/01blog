package com.dev.backend.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dev.backend.util.JwtUtil;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Autowired
    private JwtUtil jwtUtil;

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(
                500, 
                Refill.intervally(500, Duration.ofMinutes(1))
        );
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(String identifier) {
        return buckets.computeIfAbsent(identifier, k -> createNewBucket());
    }

    private String getUserIdentifier(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                return jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                return request.getRemoteAddr();
            }
        }

        return request.getRemoteAddr();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String identifier = getUserIdentifier(request);
        Bucket bucket = resolveBucket(identifier);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.setHeader("Retry-After", "30");
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getServletPath();

    return "OPTIONS".equalsIgnoreCase(request.getMethod())
            || path.startsWith("/images/")
            || path.startsWith("/videos/")
            || path.equals("/users/register")
            || path.equals("/users/login")
            || path.equals("/")
            || path.equals("/index.html")
            || path.startsWith("/css/")
            || path.startsWith("/ts/");
    }
}
