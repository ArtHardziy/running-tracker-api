package de.bergmann.runnertracker.filters;

import de.bergmann.runnertracker.service.JwtService;
import de.bergmann.runnertracker.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull  FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        var jwt = authHeader.substring(7);
        log.debug("JWT - {}", jwt);
        var username = jwtService.extractUsername(jwt);
        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            var loadedUser = userService.getUserDetailsService().loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, loadedUser)) {
                log.debug("User - {}", loadedUser);
                var securityContext = SecurityContextHolder.createEmptyContext();
                var authToken = new UsernamePasswordAuthenticationToken(
                        loadedUser, null, loadedUser.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(authToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}