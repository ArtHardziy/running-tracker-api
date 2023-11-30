package de.bergmann.runnertracker.service;

import de.bergmann.runnertracker.model.RunningTrackerUserPrincipal;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private RunningTrackerUserRepo runningTrackerUserRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userFromDb = runningTrackerUserRepo.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("RunningTrackerUser does not exist with username: [%s]", username)
                ));
        return RunningTrackerUserPrincipal.create(userFromDb);
    }

}