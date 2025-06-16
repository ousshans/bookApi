package com.renault.bookapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private static final String API_KEY_HEADER = "api-key";
    private static final String EXPECTED_KEY   = "aedz-151-ftyh-554";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String key = request.getHeader(API_KEY_HEADER);
        if (!EXPECTED_KEY.equals(key)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
