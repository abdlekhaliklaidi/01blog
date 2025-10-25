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

    String path = request.getServletPath();

    if (path.equals("/users/login") || path.equals("/users/register") || path.startsWith("/error")) {
        filterChain.doFilter(request, response);
        return;
    }

    final String authorizationHeader = request.getHeader("Authorization");
    String email = null;
    String jwt = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        email = jwtUtil.extractEmail(jwt);
    }

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (jwtUtil.validateToken(jwt)) {
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }
    }

    filterChain.doFilter(request, response);
  }

}
