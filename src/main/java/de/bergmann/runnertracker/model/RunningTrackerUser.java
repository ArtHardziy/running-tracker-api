package de.bergmann.runnertracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class RunningTrackerUser {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private int age;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getRoleType().getAuthorities().stream().toList();
    }

}