package de.bergmann.runnertracker.service.impl;

import de.bergmann.runnertracker.model.RunningTrackerUserPrincipal;
import de.bergmann.runnertracker.repositories.RunningTrackerUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RunningTrackerUserDetailsService implements UserDetailsService {

    private RunningTrackerUserRepository runningTrackerUserRepo;

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