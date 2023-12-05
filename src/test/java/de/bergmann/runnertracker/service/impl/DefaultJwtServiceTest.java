package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.RunningTrackerUserPrincipal;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.beans.JavaBean;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultJwtServiceTest {
    private RunningTrackerUserPrincipal runningTrackerUserPrincipal;
    private DefaultJwtService jwtService = new DefaultJwtService();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "jwtSecretKey","b11825876e8fc2c1e52930083de93e4d3fb7614133f36ec12e2762f6a3db9450");
        ReflectionTestUtils.setField(jwtService, "jwtExpirationMs",360000L);
        List<? extends GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        this.runningTrackerUserPrincipal = new RunningTrackerUserPrincipal(1L, "test", "test", "test", authorities);

    }

    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzAxNTkzNjA2LCJleHAiOjE3MDE1OTM5NjZ9.gMr1VqQWSGWeHmCnDs3NcpaayapwUFUXIg_3EVfx_6U
    @Test
    void extractUsername() {
        String jwtToken = jwtService.generateToken(runningTrackerUserPrincipal);
        var actual = this.jwtService.extractUsername(jwtToken);
        var expected = "test";
        assertEquals(expected, actual);
    }

    @Test
    void generateToken() {
        var actual = this.jwtService.generateToken(runningTrackerUserPrincipal);
        var expected = Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(runningTrackerUserPrincipal.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 360000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("b11825876e8fc2c1e52930083de93e4d3fb7614133f36ec12e2762f6a3db9450")), SignatureAlgorithm.HS256)
                .compact();
        assertEquals(expected, actual);
    }

    @Test
    void isTokenValid() {
        assertThrows(ResponseStatusException.class,
                () -> jwtService.isTokenValid("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzAxNTkzNjA2LCJleHAiOjE3MDE1OTM5NjZ9.gMr1VqQWSGWeHmCnDs3NcpaayapwUFUXIg_3EVfx_6U",
                        runningTrackerUserPrincipal));

    }
}