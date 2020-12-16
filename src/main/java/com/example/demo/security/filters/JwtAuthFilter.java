package com.example.demo.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.security.jwt.JwtConfig;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.security.services.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationUserService userDetails;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (authorizationHeader != null && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            try {
                String jwt = authorizationHeader.substring(7);
                String email = jwtUtil.extractEmail(jwt);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetails.loadUserByUsername(email);
                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setHeader("error-msg", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

}
