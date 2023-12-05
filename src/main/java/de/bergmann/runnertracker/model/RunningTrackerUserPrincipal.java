package de.bergmann.runnertracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class RunningTrackerUserPrincipal implements UserDetails {

    @Getter
    private Long id;
    @Getter
    private String email;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static RunningTrackerUserPrincipal create(RunnerUser runnerUser) {
        List<? extends GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        if (Objects.nonNull(runnerUser.getRole())) {
            authorities = runnerUser.getAuthorities().stream().toList();
        }
        return new RunningTrackerUserPrincipal(runnerUser.getId(),
                runnerUser.getEmail(),
                runnerUser.getUsername(),
                runnerUser.getPassword(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}