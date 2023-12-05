package de.bergmann.runnertracker.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Permission {
    READ("READ"),
    WRITE("WRITE"),
    DELETE("DELETE");

    private final String action;

    public SimpleGrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(this.getAction());
    }
}