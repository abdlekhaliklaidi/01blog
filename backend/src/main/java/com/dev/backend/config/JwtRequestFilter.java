package com.dev.backend.config;

import com.dev.backend.services.CustomUserDetailsService;
import com.dev.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) throws ServletException, IOException {

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
    filterChain.doFilter(request, response);
    return;
    }
    
    String path = request.getServletPath();
    System.out.println("Request Path: " + path);
    if (path.equals("/users") ||
    path.equals("/users/") ||
    path.startsWith("/users/login") ||
    path.startsWith("/users/register") || path.startsWith("/videos/") ||
    path.startsWith("/images/")) { 
    filterChain.doFilter(request, response);
    return;
    }

    final String authorizationHeader = request.getHeader("Authorization");
    String email = null;
    String jwt = null;

     if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        try {
            email = jwtUtil.extractEmail(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired or invalid. Please login again.");
            return;
        }
    } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authorization header missing or invalid");
        return;
    }

    System.out.println("Extracted Email: " + email);

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        System.out.println("UserDetails loaded: " + userDetails.getUsername());
        if (jwtUtil.validateToken(jwt)) {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired or invalid. Please login again.");
            return;
        }
    }

    filterChain.doFilter(request, response);

  }

}
