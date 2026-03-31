package com.example.Energy_Dashboard.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("JwtFilter - Authorization header: " + (authHeader != null ? "Present" : "Missing"));
        System.out.println("JwtFilter - Request URL: " + request.getRequestURI());
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("JwtFilter - No Bearer token, passing request");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        System.out.println("JwtFilter - Token extracted, length: " + jwt.length());
        
        try {
            String username = jwtService.extractUsername(jwt);
            System.out.println("JwtFilter - Username extracted: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("JwtFilter - UserDetails loaded, authorities: " + userDetails.getAuthorities());
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    System.out.println("JwtFilter - Token is valid");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JwtFilter - Authentication set in SecurityContext");
                } else {
                    System.out.println("JwtFilter - Token is invalid");
                }
            }
        } catch (Exception e) {
            System.out.println("JwtFilter - Error processing token: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}

