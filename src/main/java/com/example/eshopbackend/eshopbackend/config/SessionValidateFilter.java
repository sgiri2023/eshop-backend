package com.example.eshopbackend.eshopbackend.config;

import com.example.eshopbackend.eshopbackend.datamodel.SessionDataModel;
import com.example.eshopbackend.eshopbackend.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.eshopbackend.eshopbackend.session.SessionStore.SESSION_TRACKER;

@Component
public class SessionValidateFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Inside Custom Filter Chain");
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("spring-")) {
            System.out.println("Session Key: " + authHeader);
            token = authHeader;
            SessionDataModel sessionData = SESSION_TRACKER.get(token);
            System.out.println("Session Details: " + sessionData);
            username = sessionData.getUserName();
        } else {
            System.out.println("No Header Present");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Session Key Authenticated: " + username);
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
